package controllers

import play.api.mvc._
import models._
import play.api.data.Forms._

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._


import play.api.data._

import anorm._

import play.api.libs.json.Json._
import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsObject
import play.api.libs.Jsonp


object QRCodes extends Controller with Secured {
  val qrcodeForm = Form(
      mapping(
        "id" -> ignored(NotAssigned:Pk[Long]),
        "evento_id" -> default(number,1),
        "texto" -> nonEmptyText,
        "tipo" -> nonEmptyText,
        "resposta" -> default(text,"N/A"),
        "alternativas" -> default(text,"N/A"),
        "pontuacao" -> default(number,0)
      )(QRCode.apply)(QRCode.unapply)
      verifying ("Texto não pode conter o caracter '#'", form => form.texto.indexOf('#') == -1)
    )

  def formQRCode = withAuth { user => implicit request =>
    Ok(views.html.formQRCode(QRCode.all(), qrcodeForm))
  }

  def renderQRCode(size: Int) = withAuth { user => implicit request =>
    val zipado ="novo texto get"
    val id: Int = 1
    Ok(views.html.renderQRCode(zipado, size,id))
  }

  def renderQRCodePost() = withAuth { user => implicit request =>
    val size = request.body.asFormUrlEncoded.get("size")(0).toInt
    val id = request.body.asFormUrlEncoded.get("id")(0).toInt
    val zipado = "SERPRO#JSON#"+id
    Ok(views.html.renderQRCode(zipado, size,id))
  }

  def gerarQRCode = withAuth { user => implicit request =>
    qrcodeForm.bindFromRequest.fold(
        errors => BadRequest(views.html.formQRCode(QRCode.all(),errors)),
        qrcode => {
          QRCode.create(qrcode.evento_id,qrcode.texto, qrcode.tipo, qrcode.resposta,qrcode.alternativas,qrcode.pontuacao)
          Redirect(routes.QRCodes.formQRCode)
        }
      )
    }

  def deleteQRCode(id: Long) = withAuth { user => implicit request =>
    QRCode.delete(id)
    Redirect(routes.QRCodes.formQRCode)
  }

  def ranking = withAuth { user => implicit request =>
    Ok(views.html.ranking(Resposta.obtemPontuacaoTodosUsuarios()))
  }


  def renderQRCodeJSON(id: Long) = Action {
    var json = toJson(
      Map(
        "status" -> "ERRO",
        "msgRet" -> "QRCode não encontrado.")
    )

    QRCode.findById(id).map{ q :QRCode =>
      json = toJson(q)
    }

    Ok(Jsonp("callback",json))
  }



}
