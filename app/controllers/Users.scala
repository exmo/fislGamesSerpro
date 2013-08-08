package controllers

import play.api.mvc._
import models._
import play.api.libs.Jsonp


import play.api.libs.json.Json._


object Users extends Controller with Secured {
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
  def consultarUsuario(email: String) = Action {
    var json = toJson(
      Map("codRet" -> "ERRO", "msgRet" -> s"Usuario $nome inexistente")
    )
    Usuario.findByEmail(email).map { usuario =>
      json = toJson(
        Map("codRet" -> "OK", "msgRet" -> s"Usuario $email encontrado", "nome" -> usuario.nome, "telefone" -> usuario.telefone)
      )
    }
    Ok(Jsonp(callback, json))
  }  
}
