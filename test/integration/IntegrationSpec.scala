package integration

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import org.fluentlenium.core.filter.FilterConstructor._


/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
class IntegrationSpec extends Specification with WithTestDatabase {
 /*
  "Application" should {

    "work from within a browser" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")
        browser.$("form input", withName("email")).first.getAttribute("required") must equalTo("true")
      }

    }


  }   */
}