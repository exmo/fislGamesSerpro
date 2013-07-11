package controllers

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import integration.WithTestDatabase

class ApplicationSpec extends Specification with WithTestDatabase {

  // --
  
  "Application" should {


    "ir para a pagina de login se usuario nao for autenticado" in {
      running(FakeApplication(/*additionalConfiguration = inMemoryDatabase()*/)) {
        val result  = route( FakeRequest( GET, "/")).get
        status(result) must beEqualTo(303)
      }
    }

    "redirecionar para a lista de qrcodes /" in {
      
      val result = controllers.Application.index(FakeRequest())
      
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome.which(_ == "/qrcode/form")
      
    }
    
  }

}