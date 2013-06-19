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

  val resps = {
    get[Pk[Long]]("idQrCode")~
      get[String]("texto")~
      get[Pk[String]]("email")~
      get[String]("resposta")~
      get[Long]("pontuacao") map {
      case idQrCode~texto~email~resposta~pontuacao => Seq(idQrCode,texto,email,resposta,pontuacao)
    }
  }

  def all(): List[Resposta] = DB.withConnection { implicit c =>
    SQL("select * from resposta").as(resp *)
  }

  def findByIdQrCodeEmail(idQrCode: Long, email: String): Option[Resposta] = DB.withConnection { implicit c =>
    SQL("select * from resposta where idQrCode={idQrCode} and email={email}").on('email -> email,'idQrCode -> idQrCode).as(Resposta.resp.singleOpt)
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
        "join resposta r on u.email = r.email group by u.email, u.nome order by pontuacao desc").as(users *)

  }

  def obtemRespostasUsuario(email: String): List[Seq[Any]] = DB.withConnection { implicit c =>
  //def obtemRespostasUsuario(email: String): List[Resposta] = DB.withConnection { implicit c =>
    //SQL("select * from resposta where email={email}").on('email -> email).as(resp *)
    SQL("select r.idQrCode, q.texto, r.email, r.resposta, r.pontuacao from resposta  r " +
      "inner join qrcode q on q.id = r.idQrCode where r.email={email}").on('email -> email).as(resps *)
  }
}
