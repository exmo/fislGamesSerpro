package controllers

import play.api.mvc._
import models._
import play.api.data.Forms._

import play.api.libs.json.Writes._


import play.api.data._

import anorm._

import play.api.libs.json.Json._
import play.api.libs.Jsonp
import play.api.libs.json.JsValue
import scala.util.control.Exception


object Palestras extends Controller with Secured {
  val palestraForm = Form(
      mapping(
        "id" -> ignored(NotAssigned:Pk[Long]),
        "evento_id" -> default(number,1),
        "descricao" -> nonEmptyText,
        "data" -> nonEmptyText,
        "palestrante" -> default(text,"")
      )(Palestra.apply)(Palestra.unapply)
    )

  def listar = withAuth { user => implicit request =>
    Ok(views.html.palestra(Palestra.all(), palestraForm))
  }

  def cadastrar = withAuth { user => implicit request =>
    palestraForm.bindFromRequest.fold(
      errors => BadRequest(views.html.palestra(Palestra.all(),errors)),
      p => {

        val id = request.body.asFormUrlEncoded.get("id")(0).toInt
        if(id != 0) {
          Palestra.update(id,p.descricao,p.data,p.palestrante,p.evento_id)
        } else {
          Palestra.create(p.descricao,p.data,p.palestrante,p.evento_id)
        }

        Redirect(routes.Palestras.listar)
      }
    )
  }

  def excluir(id: Long) = withAuth { user => implicit request =>
    Palestra.delete(id)
    Redirect(routes.Palestras.listar)
  }


  def listarAsJson = Action {
    var lista: List[Map[String,JsValue]] = Palestra.all().map { p =>
      Map(
        "id" -> toJson(p.id.get),
        "descricao" -> toJson(p.descricao),
        "data" -> toJson(p.data),
        "palestrante" -> toJson(p.palestrante)
        )
    }
    val json = toJson(
      Map("status" -> toJson("OK"),
        "palestras" -> toJson(lista)
      )
    )
    Ok(json)
  }

  def registrarParticipacao(palestraid: Long, email: String, inOut: String) = Action {
    var json = toJson(
      Map("codRet" -> "ERRO", "msgRet" -> s"Palestra $palestraid ou Usuario $email inexistente")
    )
    Usuario.findByEmail(email).map { usuario =>
      Palestra.findById(palestraid).map { palestra =>
        try {
          Palestra.registrarParticipacao(palestraid, email, inOut)
          json = toJson(
            Map("codRet" -> "OK", "msgRet" -> s"Registrada participacao do $email na palestra ${palestra.descricao}")
          )
        } catch {
          case e: Exception =>
            json = toJson(
              Map("codRet" -> "ERRO", "msgRet" -> s"Usuário $email já realizou esta operação na palestra $palestraid")
          )
        }
      }
    }
    Ok(json)
  }

}
