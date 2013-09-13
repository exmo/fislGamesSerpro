package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Admin (
                     var id:Pk[Long],
                     var email:String,
                     var senha:String
                     )

object Admin {

  val admin = {
    get[Pk[Long]]("id")~
      get[String]("email") ~
      get[String]("senha") map {
      case id~email~senha => Admin(id,email,senha)
    }
  }


  def autenticate(email: String, senha: String): Option[Admin] = DB.withConnection { implicit c =>
    SQL("select * from admin where email={email} and senha={senha}").on('email -> email, 'senha -> senha).as(admin.singleOpt)
  }


}

