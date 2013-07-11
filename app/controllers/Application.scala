package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.Jsonp

import views._

import play.api.data._

import anorm._

import play.api.libs.json.Json._

import play.api.mvc.Security
import org.joda.time.DateTime

object Application extends Controller with Secured {

  def index = Action {
    Redirect(routes.QRCodes.formQRCode)
  }

}
