package controllers

import play.api.mvc._
import models._
import play.api.libs.Jsonp


import play.api.libs.json.Json._


object Desafios extends Controller with Secured {

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

  def todasRespostas(id: Long) = withAuth { user => implicit request =>

    var pergunta = ""
    var respostaCorreta = ""

    QRCode.findById(id).map { qrcode =>
      pergunta = qrcode.texto
      respostaCorreta = qrcode.resposta
    }
    Ok(views.html.todasrespostas(Resposta.obtemTodasRespostas(id),id,pergunta,respostaCorreta))
  }

  def responder(id: Long, email: String, resposta: String, callback: String) = Action {
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
        var respostaCorreta = desafio.resposta.toUpperCase.trim
        var respostaUsuario = resposta.toUpperCase.trim

        if (respostaCorreta == respostaUsuario) {
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
  
  def considerarRespostaCorreta(id: Long, email: String) = withAuth { user => implicit request =>
    QRCode.findById(id).map { desafio =>
      Resposta.update(id,email,desafio.pontuacao);
    }
    Redirect(routes.Desafios.respostas(email))
  }  
  
}
