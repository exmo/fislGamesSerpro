package controllers

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import integration.WithTestDatabase
import org.specs2.specification.Scope
import models.QRCode
import play.api.test.FakeApplication
import play.api.test.FakeApplication

class PalestraSpec extends Specification with WithTestDatabase {
     /*
  "QRCodes" should {


    "listar as Palestras na primeira pagina (com credenciais)" in around {

      Palestra.create("Palestra 1", "08/08/2013", "Serge", 1)

      val result = route(FakeRequest(GET, "/palestra/form").withSession("username" -> "admin")).get

      status(result) must equalTo(OK)
      contentAsString(result) must contain("1 Palestra")

    }


    "redirecionar para a pagina de login se usuario nao se autenticou" in around {

      val result = controllers.Application.index(FakeRequest())

      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome.which(_ == "/login")

    }
  }


  class Palestra extends Scope {
    val palestraId = Palestra.create("Palestra 1", "08/08/2013", "Serge", 1)
  }
  */
}