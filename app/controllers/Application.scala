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
          Redirect(routes.Application.formQRCode)
        }
      )
    }

  def deleteQRCode(id: Long) = withAuth { user => implicit request =>
    QRCode.delete(id)
    Redirect(routes.Application.formQRCode)
  }

  def ranking = withAuth { user => implicit request =>
    Ok(views.html.ranking(Resposta.obtemPontuacaoTodosUsuarios()))
  }

  def respostas(email: String) = withAuth { user => implicit request =>
    var nomeUsuario = ""
    Usuario.findByEmail(email).map { usuario =>
      nomeUsuario = usuario.nome
    }
    var pontos = Resposta.obtemPontuacaoUsuario(email)
    Ok(views.html.resposta(Resposta.obtemRespostasUsuario(email), nomeUsuario, email, pontos))    
  }

  // -- REST/JSONP

  def criarUsuario(email: String, nome: String, telefone: String, callback: String) = Action {
    var json = toJson(
      Map("codRet" -> "OK", "msgRet" -> s"Usuario $nome cadastrado com sucesso!")
    )
    Usuario.findByEmail(email).map { usuario =>
      json = toJson(
        Map("codRet" -> "ERRO", "msgRet" -> s"Usuario $email ja estava cadastrado")
      )
    }.getOrElse {
      Usuario.create(email,nome,telefone)
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
  
  def considerarRespostaCorretaDesafio(id: Long, email: String) = withAuth { user => implicit request =>
    QRCode.findById(id).map { desafio =>
      Resposta.update(id,email,desafio.pontuacao);
    }
    Redirect(routes.Application.respostas(email))
  }  
  
}
