package controllers

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import integration.WithTestDatabase
import org.specs2.specification.Scope
import models.QRCode
import play.api.test.FakeApplication
import play.api.test.FakeApplication

class QRCodesSpec extends Specification with WithTestDatabase {

  "QRCodes" should {


    "listar os QRCodes na primeira pagina (com credenciais)" in around {

      QRCode.create("QRCODE TEST", "INFO", "5", "N/A", "N/A", "N/A", 10)

      val result = route(FakeRequest(GET, "/qrcode/form").withSession("username" -> "admin")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("1 QRCode(s)")

    }

  }

  /*
  class qrcodes extends Scope {
    val qrcodeId = QRCode.create("QRCODE TEST", "INFO", "5", "N/A", "N/A", "N/A", 10)
  } */

}