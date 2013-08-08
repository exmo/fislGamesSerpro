package models

import org.specs2.mutable._
import integration.WithTestDatabase
import org.specs2.specification.Scope


class RespostaSpec extends Specification with WithTestDatabase {
  /*
  "Resposta.all" should {
    "retornar uma lista nao vazia" in new resps {
      Resposta.all must not beEmpty
    }
  }

  "Resposta.obtemRespostasUsuario test@test.com" should {
    "retornar uma lista de tamanho 1" in new resps {
      Resposta.obtemRespostasUsuario(userId).size must beEqualTo(1)
    }
  }

  "Resposta.obtemRespostasUsuario none@mail.com" should {
    "retornar uma lista de tamanho 0" in new resps {
      Resposta.obtemRespostasUsuario("none@mailcom").size must beEqualTo(0)
    }
  }

  "Resposta.obtemPontuacaoUsuario test@test.com" should {
    "retornar valor 10" in new resps {
      Resposta.obtemPontuacaoUsuario(userId).longValue() must beEqualTo(10)
    }
  }

  "Resposta.obtemPontuacaoToposUsuarios" should {
    "retornar uma lista de tamanho 1" in new resps {
      Resposta.obtemPontuacaoTodosUsuarios.size must beEqualTo(1)
    }
  }

  "Resposta.update atualizando pontuacao do usuario test@test.com para 40" should {
    "retornar 40 pontos" in new resps {
      Resposta.update(qrcodeId, userId, 40)
      Resposta.obtemPontuacaoUsuario(userId).longValue() must beEqualTo(40)
    }
  }

  // Este código deve ser executado antes de cada teste
  class resps extends Scope {
    val userId = "test@test.com"
    val qrcodeId = QRCode.create("QRCODE TEST", "DESAFIO", "2+2", "4", "N/A", "N/A", 10)
    Usuario.create(userId, "USUARIO TEST", "5566778899")
    Resposta.create(qrcodeId,userId,"4", 10)
  }     */

}