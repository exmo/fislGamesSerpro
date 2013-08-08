package models

import org.specs2.mutable._
import integration.WithTestDatabase
import org.specs2.specification.Scope
import org.specs2.matcher._


class QRCodeSpec extends Specification with WithTestDatabase {
      /*
  "QRCode.all" should {
    "retornar uma lista nao vazia" in new qrcodes {
      QRCode.all must not beEmpty
    }
  }

  "QRCode.all depois de um insert" should {
    "retornar um qrcode" in new qrcodes {
      QRCode.all.size must beEqualTo(1)
    }
  }

  "QRCode.delete" should {
    "remover um registro" in new qrcodes {
      val ret = QRCode.delete(qrcodeId)
      ret must beEqualTo(1)
      QRCode.all must beEmpty
    }
  }

  "QRCode.findById em um registro recem inserido" should {
    "retornar o ultimo registro inserido" in new qrcodes {
      QRCode.findById(qrcodeId).map { qrcode =>
        qrcode.texto must beEqualTo("QRCODE TEST")
      }
    }
  }

  "QRCode.findById em um registro inexistente" should {
    "deve gerar um getOrElse()" in new qrcodes {
      QRCode.findById(1234).map { qrcode =>
        failure("não deveria ter encontrado um registro com id 123")
      }.getOrElse {
        success
      }
    }
  }

  "QRCode.delete em um registro que não existe" should {
    "não afetar nada" in  {
      val ret = QRCode.delete(123)
      ret must beEqualTo(0)
      QRCode.all must beEmpty
    }
  }
   */

  // Este código deve ser executado antes de cada teste
  /*
  class qrcodes extends Scope {
    val qrcodeId = QRCode.create("QRCODE TEST", "INFO", "5", "N/A", "N/A", "N/A", 10)
  }
    */
}