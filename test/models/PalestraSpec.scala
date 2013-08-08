package models

import org.specs2.mutable._
import integration.WithTestDatabase
import org.specs2.specification.Scope


class PalestraSpec extends Specification with WithTestDatabase {

  "Palestra.all" should {
    "retornar uma lista nao vazia" in new palestras {
      Palestra.all must not beEmpty
    }
  }

  "Palestra.all depois de um insert" should {
    "retornar um unico Palestra" in new palestras {
      Palestra.all.size must beEqualTo(1)
    }
  }

  "QRCode.delete" should {
    "remover um registro" in new palestras {
      Palestra.delete(1)
      Palestra.all must beEmpty
    }
  }

/*  "Palestra.findById em um registro recem inserido" should {
    "retornar o ultimo Palestra inserido com nome Palestra TEST" in new palestras {
      Palestra.findByEmail("test@test.com").map { Palestra =>
        Palestra.nome must beEqualTo("Palestra TEST")
      }.getOrElse {
        failure("Deveria ter encontrado dum Palestra teste@test.com")
      }
    }
  }

  "Palestra.findById em um registro inexistente" should {
    "deve gerar um getOrElse()" in new palestras {
      Palestra.findByEmail("null@mail.com").map { qrcode =>
        failure("Não deveria ter encontrado um Palestra com email null@mail.com")
      }.getOrElse {
        success
      }
    }
  }
*/

  "Palestra.delete em um registro que não existe" should {
    "não afetar nada" in  {
      val ret = Palestra.delete(10)
      ret must beEqualTo(0)
      Palestra.all must beEmpty
    }
  }

  // Este código deve ser executado antes de cada teste
  class palestras extends Scope {
    val palestraId = Palestra.create("Palestra 1", "08/08/2013", "Serge", 1)
  }

}