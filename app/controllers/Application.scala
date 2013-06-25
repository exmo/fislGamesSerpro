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

import play.api.mvc.Security
import org.joda.time.DateTime

object Application extends Controller with Secured {

  // -- Authentication

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Email ou senha inválidos", result => result match {
      case (email, password) => check(email, password)
    })
  )

  def check(username: String, password: String) = {
    (username == "admin" && password == "1234")
  }

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Application.formQRCode).withSession(Security.username -> user._1)
    )
  }

  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "Logout efetuado com sucesso."
    )
  }

  // -- Web

  val qrcodeForm = Form(
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "texto" -> nonEmptyText,
      "tipo" -> nonEmptyText,
      "resposta" -> default(text,"N/A"),
      "pontuacao" -> default(number,0)
    )(QRCode.apply)(QRCode.unapply)
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
          QRCode.create(qrcode.texto, qrcode.tipo, qrcode.resposta, qrcode.pontuacao)
          Redirect(routes.Application.formQRCode())
        }
      )
    }

  def deleteQRCode(id: Long) = withAuth { user => implicit request =>
    QRCode.delete(id)
    Redirect(routes.Application.formQRCode())
  }

  def ranking = withAuth { user => implicit request =>
    Ok(views.html.ranking(Resposta.obtemPontuacaoTodosUsuarios()))
  }

  def respostas(email: String, nome: String, pontuacao: Int) = withAuth { user => implicit request =>
    Ok(views.html.resposta(Resposta.obtemRespostasUsuario(email), nome, pontuacao))
  }

  // -- REST/JSONP

  def criarUsuario(email: String, nome: String, callback: String) = Action {
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
  }


  def responderDesafio(id: Long, email: String, resposta: String, callback: String) = Action {
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
}

trait Secured {

  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  /**
   * This method shows how you could wrap the withAuth method to also fetch your user
   * You will need to implement UserDAO.findOneByUsername
   */
  /*
  def withUser(f: User => Request[AnyContent] => Result) = withAuth { username => implicit request =>
    UserDAO.findOneByUsername(username).map { user =>
      f(user)(request)
    }.getOrElse(onUnauthorized(request))
  } */
}

