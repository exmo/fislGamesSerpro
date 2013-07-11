package controllers

import play.api.mvc._
import models._
import play.api.data.Forms._
import play.api.libs.Jsonp


import play.api.data._

import anorm._

import play.api.libs.json.Json._


object QRCodes extends Controller with Secured {
  val qrcodeForm = Form(
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "texto" -> nonEmptyText,
      "tipo" -> nonEmptyText,
      "resposta" -> default(text,"N/A"),
      "alternativa1" -> default(text,"N/A"),
      "alternativa2" -> default(text,"N/A"),
      "alternativa3" -> default(text,"N/A"),
      "textoQrCode" -> default(text,"N/A"),
      "pontuacao" -> default(number,0)
    )(QRCode.apply)(QRCode.unapply)
    verifying ("Texto não pode conter o caracter '#'", form => form.texto.indexOf('#') == -1)
  )

  def formQRCode = withAuth { user => implicit request =>
    Ok(views.html.formQRCode(QRCode.all(), qrcodeForm))
  }

  def renderQRCode(texto: String, size: Int) = withAuth { user => implicit request =>
    Ok(views.html.renderQRCode(texto, size))
  }

  def renderQRCodePost() = withAuth { user => implicit request =>
    val texto = request.body.asFormUrlEncoded.get("texto")(0)
    val size = request.body.asFormUrlEncoded.get("size")(0).toInt
    Ok(views.html.renderQRCode(texto, size))
  }

  def gerarQRCode = withAuth { user => implicit request =>
    qrcodeForm.bindFromRequest.fold(
        errors => BadRequest(views.html.formQRCode(QRCode.all(),errors)),
        qrcode => {
          QRCode.create(qrcode.texto, qrcode.tipo, qrcode.resposta,qrcode.alternativa1,qrcode.alternativa2,qrcode.alternativa3,qrcode.pontuacao)
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
}
