package controllers

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import integration.WithTestDatabase

class LoginSpec extends Specification with WithTestDatabase {

  // --
  
  "Login" should {


    "ir para a pagina de login" in {
      running(FakeApplication()) {
        val result  = route( FakeRequest( GET, "/login")).get
        status(result) must beEqualTo(200)
        contentAsString(result) must contain("Login")
        contentAsString(result) must contain("Email")
        contentAsString(result) must contain("Senha")
      }
    }

    "redirecionar para a lista de qrcodes /" in {
      
      val result = controllers.Application.index(FakeRequest())
      
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome.which(_ == "/qrcode/form")
      
    }
  }

  "Login form " should {

    "apresentar erro se usuario ou senha for invalido" in {
      val form = Login.loginForm.bind(Map("email" -> "admin",
        "password" -> "wrongpass"))

      form.hasErrors must beTrue
      form.errors.size must not equalTo(0)

      form("email").hasErrors must beFalse
      form("password").hasErrors must beFalse
    }

    "nao apresentar erro se usuario e senha forem validos" in {
      val form = Login.loginForm.bind(Map("email" -> "admin",
        "password" -> "bahia88"))

      form.hasErrors must beFalse
      form.errors.size must equalTo(0)

      form("email").hasErrors must beFalse
      form("password").hasErrors must beFalse

      //redirectLocation(result) must beSome.which(_ == "/qrcode/form")
    }
  }

}