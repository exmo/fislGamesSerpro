package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Usuario (
    var email:Pk[String],
    var nome:String,
    var telefone:String
)

object Usuario {

  val usuario = {
      get[Pk[String]]("email")~
      get[String]("nome") ~
      get[String]("telefone") map {
      case email~nome~telefone => Usuario(email,nome,telefone)
    }
  }

  def all(): List[Usuario] = DB.withConnection { implicit c =>
    SQL("select * from usuario").as(usuario *)
  }

  def findByEmail(email: String): Option[Usuario] = DB.withConnection { implicit c =>
    SQL("select * from usuario where email={email}").on('email -> email).as(Usuario.usuario.singleOpt)
  }

  def create(email: String, nome: String, telefone: String) {
    DB.withConnection { implicit c =>
      SQL("insert into usuario (email, nome, telefone) values ({email},{nome},{telefone})").on(
        'email -> email,
        'nome -> nome,
        'telefone -> telefone
      ).executeUpdate()
    }
  }

  def delete(email: String): Long = {
    DB.withConnection { implicit c =>
      return SQL("delete from usuario where email = {email}").on('email -> email).executeUpdate()
    }
  }

}

