package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Resposta (
       var idQrCode:Pk[Long],
       var email:Pk[String],
       var resposta:String,
       var pontuacao:Long
)

object Resposta {

  val resp = {
    get[Pk[Long]]("idQrCode")~
    get[Pk[String]]("email")~
    get[String]("resposta")~
    get[Long]("pontuacao") map {
      case idQrCode~email~resposta~pontuacao => Resposta(idQrCode,email,resposta,pontuacao)
    }
  }

  val users = {
    get[String]("email")~
      get[String]("nome")~
      get[Long]("pontuacao") map {
      case email~nome~pontuacao => Seq(email,nome,pontuacao)
    }
  }

  def all(): List[Resposta] = DB.withConnection { implicit c =>
    SQL("select * from resposta").as(resp *)
  }

  def create(idQrCode:Long,email: String, resposta: String, pontuacao: Long) {
    DB.withConnection { implicit c =>
      SQL("insert into resposta (idQrCode,email,resposta,pontuacao) values ({idQrCode},{email},{resposta},{pontuacao})").on(
        'idQrCode -> idQrCode,
        'email -> email,
        'resposta -> resposta,
        'pontuacao -> pontuacao
      ).executeUpdate()
    }
  }

  def obtemPontuacaoTodosUsuarios(): List[Seq[Any]] = DB.withConnection { implicit c =>
    SQL("select u.email, u.nome, coalesce(sum(r.pontuacao),0) as pontuacao from usuario u left " +
        "join resposta r on u.email = r.email group by u.email, u.nome").as(users *)

  }
}
