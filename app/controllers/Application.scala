package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._

import views._

import play.api.data._

import anorm._

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

  def renderQRCode(texto: String) = Action {
    Ok(views.html.renderQRCode(texto))
  }

  val renderQRCodePost = Action { implicit request =>
    val texto = request.body.asFormUrlEncoded.get("texto")(0)
    Ok(views.html.renderQRCode(texto))
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

  def criarUsuario(email: String, nome: String) = Action{
    Usuario.create(email,nome);
    Ok(s"$nome")
  }

  def ranking = Action {
    //Ok("ok")//views.html.ranking(Usuario.all()))
    Ok(views.html.ranking(Resposta.obtemPontuacaoTodosUsuarios()))
  }

  def responderDesafio(id: Long, email: String, resposta: String) = Action{
    QRCode.findById(id).map { desafio =>
      var pontos = 0
      var mensagem = "ERROU!!"
      if (desafio.resposta == resposta) {
        pontos = desafio.pontuacao
        mensagem = "ACERTOU!!"
      }
      Resposta.create(id,email,resposta, pontos);
      Ok(s"$mensagem")
    }.getOrElse {
      Ok("NOT FOUND")
    }
  }

}