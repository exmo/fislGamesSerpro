package models

import org.specs2.mutable._
import integration.WithTestDatabase
import org.specs2.specification.Scope


class UsuarioSpec extends Specification with WithTestDatabase {

  "Usuario.all" should {
    "retornar uma lista nao vazia" in new users {
      Usuario.all must not beEmpty
    }
  }

  "Usuario.all depois de um insert" should {
    "retornar um unico usuario" in new users {
      Usuario.all.size must beEqualTo(1)
    }
  }

  "QRCode.delete" should {
    "remover um registro" in new users {
      Usuario.delete("test@test.com")
      Usuario.all must beEmpty
    }
  }

  "Usuario.findById em um registro recem inserido" should {
    "retornar o ultimo usuario inserido com nome USUARIO TEST" in new users {
      Usuario.findByEmail("test@test.com").map { usuario =>
        usuario.nome must beEqualTo("USUARIO TEST")
      }.getOrElse {
        failure("Deveria ter encontrado dum usuario teste@test.com")
      }
    }
  }

  "Usuario.findById em um registro inexistente" should {
    "deve gerar um getOrElse()" in new users {
      Usuario.findByEmail("null@mail.com").map { qrcode =>
        failure("Não deveria ter encontrado um usuario com email null@mail.com")
      }.getOrElse {
        success
      }
    }
  }

  "Usuario.delete em um registro que não existe" should {
    "não afetar nada" in  {
      val ret = Usuario.delete("none@mail.com")
      ret must beEqualTo(0)
      Usuario.all must beEmpty
    }
  }

  // Este código deve ser executado antes de cada teste
  class users extends Scope {
    val userId = Usuario.create("test@test.com", "USUARIO TEST", "5566778899")
  }

}