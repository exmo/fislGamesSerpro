package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.Jsonp

import views._

import play.api.data._

import anorm._

import play.api.libs.json.Json._

object Application extends Controller {

  val qrcodeForm = Form(
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "texto" -> nonEmptyText,
      "tipo" -> nonEmptyText,
      "resposta" -> default(text,"N/A"),
      "pontuacao" -> default(number,0)
    )(QRCode.apply)(QRCode.unapply)
  )


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def formQRCode = Action {
    Ok(views.html.formQRCode(QRCode.all(), qrcodeForm))
  }

  def renderQRCode(texto: String, size: Int) = Action {
    Ok(views.html.renderQRCode(texto, size))
  }

  def renderQRCodePost() = Action { implicit request =>
    val texto = request.body.asFormUrlEncoded.get("texto")(0)
    val size = request.body.asFormUrlEncoded.get("size")(0).toInt
    Ok(views.html.renderQRCode(texto, size))
  }

  def gerarQRCode = Action { implicit request =>
    qrcodeForm.bindFromRequest.fold(
        errors => BadRequest(views.html.formQRCode(QRCode.all(),errors)),
        qrcode => {
          QRCode.create(qrcode.texto, qrcode.tipo, qrcode.resposta, qrcode.pontuacao)
          Redirect(routes.Application.formQRCode())
        }
      )
    }

  def deleteQRCode(id: Long) = Action {
    QRCode.delete(id)
    Redirect(routes.Application.formQRCode())
  }

  def criarUsuario(email: String, nome: String, callback: String) = Action{
    var json = toJson(
      Map("codRet" -> "OK", "msgRet" -> s"Usuario $nome cadastrado com sucesso!")
    )
    Usuario.findByEmail(email).map { usuario =>
      json = toJson(
        Map("codRet" -> "ERRO", "msgRet" -> s"Usuario $email ja estava cadastrado")
      )
    }.getOrElse {
      Usuario.create(email,nome)
    }
    Ok(Jsonp(callback, json))
    /*
    Ok(toJson(
        Map("codRet" -> "OK", "msgRet" -> s"Usuario $nome cadastrado com sucesso!")
      )
    ).as("application/json")
    */
  }

  def ranking = Action {
    //Ok("ok")//views.html.ranking(Usuario.all()))
    Ok(views.html.ranking(Resposta.obtemPontuacaoTodosUsuarios()))
  }

  def respostas(email: String, nome: String, pontuacao: Int) = Action {
    Ok(views.html.resposta(Resposta.obtemRespostasUsuario(email), nome, pontuacao))
  }

  def responderDesafio(id: Long, email: String, resposta: String, callback: String) = Action{
    var json = toJson(
      Map("status" -> "OK", "codRet" -> "ERRO-RE", "msgRet" -> "Resposta errada!")
    )
    Resposta.findByIdQrCodeEmail(id, email).map { resp =>
      json = toJson(
        Map("status" -> "OK",  "codRet" -> "ERRO-DJR", "msgRet" -> "Este desafio já foi respondido por você!")
      )
    }.getOrElse {
      QRCode.findById(id).map { desafio =>
        var pontos = 0
        if (desafio.resposta == resposta) {
          pontos = desafio.pontuacao
          json = toJson(
            Map("status" -> "OK", "codRet" -> "OK", "msgRet" -> "Resposta correta!")
          )
        }
        Resposta.create(id,email,resposta, pontos);
      }.getOrElse {
        json = toJson(
          Map("status" -> "OK", "codRet" -> "ERRO-DNE", "msgRet" -> "DESAFIO NAO ENCONTRADO")
        )
      }
    }

    Ok(Jsonp(callback, json))
  }

  def myService(callback: String) = Action {
    val json = toJson(
      Map("codRet" -> "OK", "msgRet" -> "FUNFOU")
    )
    Ok(Jsonp(callback, json))
    //BadRequest(Jsonp(callback, json))
  }


  /* JSON SAMPLE
  def getTask(id: Long) = Action {
    Task.findById(id).map { task =>
      Ok(toJson(
        Map("status" -> "OK", "task" -> (s"Task ${task.id} -> " + task.label))
      )).as("application/json")
    }.getOrElse {
      Ok("NOT FOUND")
    }
  }
  */

}