package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Usuario (
    var email:Pk[String],
    var nome:String
)

object Usuario {

  val usuario = {
      get[Pk[String]]("email")~
      get[String]("nome") map {
      case email~nome => Usuario(email,nome)
    }
  }

  def all(): List[Usuario] = DB.withConnection { implicit c =>
    SQL("select * from usuario").as(usuario *)
  }

  def create(email: String, nome: String) {
    DB.withConnection { implicit c =>
      SQL("insert into usuario (email, nome) values ({email},{nome})").on(
        'email -> email,
        'nome -> nome
      ).executeUpdate()
    }
  }

  def delete(email: String) {
    DB.withConnection { implicit c =>
      SQL("delete from usuario where email = {email}").on('email -> email).executeUpdate()
    }
  }

}

