package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

import org.joda.time._
import org.joda.time.format._

import java.math.BigDecimal

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
      get[String]("telefone")~
      get[BigDecimal]("pontuacao") map {
      case email~nome~telefone~pontuacao => Seq(email,nome,telefone,pontuacao)
    }
  }

  val resps = {
    get[Pk[Long]]("idQrCode")~
      get[String]("texto")~
      get[Pk[String]]("email")~
      get[String]("qrcode.resposta")~
      get[String]("resposta")~
      get[Long]("pontuacao")~
      get[String]("ultima_atualizacao") map {
      case idQrCode~texto~email~resposta_correta~resposta~pontuacao~ultima_atualizacao => Seq(idQrCode,texto,email,resposta_correta,resposta,pontuacao,formataDataHora(ultima_atualizacao))
    }
  }
  
  def formataDataHora(dataHora: String): String = {
    var dt = new DateTime(dataHora)
    var fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
    return fmt.print(dt)
  }


  def all(): List[Resposta] = DB.withConnection { implicit c =>
    SQL("select * from resposta").as(resp *)
  }

  def findByIdQrCodeEmail(idQrCode: Long, email: String): Option[Resposta] = DB.withConnection { implicit c =>
    SQL("select * from resposta where idQrCode={idQrCode} and email={email}").on('email -> email,'idQrCode -> idQrCode).as(Resposta.resp.singleOpt)
  }

  def create(idQrCode:Long,email: String, resposta: String, pontuacao: Long) {
    DB.withConnection { implicit c =>
      SQL("insert into resposta (idQrCode,email,resposta,pontuacao,ultima_atualizacao) values ({idQrCode},{email},{resposta},{pontuacao},'"+new DateTime()+"')").on(
        'idQrCode -> idQrCode,
        'email -> email,
        'resposta -> resposta,
        'pontuacao -> pontuacao
      ).executeUpdate()
    }
  }

  def update(idQrCode:Long, email: String, pontuacao: Long) {
    DB.withConnection { implicit c =>
      SQL("update resposta set resposta=CONCAT(resposta,\" (considerada Correta pela Organização)\") , pontuacao={pontuacao}, ultima_atualizacao='"+new DateTime()+"'" +
        " where idQrCode={idQrCode} and email={email}").on(
        'idQrCode -> idQrCode,
        'email -> email,
        'pontuacao -> pontuacao
      ).executeUpdate()
    }
  }
  
  def obtemPontuacaoTodosUsuarios(): List[Seq[Any]] = DB.withConnection { implicit c =>
    SQL("select u.email, u.nome, u.telefone, coalesce(sum(r.pontuacao),0) as pontuacao from usuario u left " +
        "join resposta r on u.email = r.email group by u.email, u.nome, u.telefone order by pontuacao desc").as(users *)

  }



  def obtemRespostasUsuario(email: String): List[Seq[Any]] = DB.withConnection { implicit c =>
    SQL("select r.idQrCode, q.texto, r.email, q.resposta, r.resposta, r.pontuacao, r.ultima_atualizacao from resposta  r " +
      "inner join qrcode q on q.id = r.idQrCode where r.email={email}").on('email -> email).as(resps *)
  }
  
  def obtemPontuacaoUsuario(email: String) = DB.withConnection { implicit c =>
    SQL("select coalesce(sum(pontuacao),0) as pontuacao from resposta where email = {email}").on('email -> email).as(scalar[BigDecimal].single)

  }

  def obtemProgresso(email: String)  = DB.withConnection { implicit c =>
    SQL("select FORMAT(FLOOR((temp1.x / temp2.y)*100),0) from (select count(*) as x from resposta where email={email}) as temp1, (select count(*) as y from qrcode where tipo like 'DESAFIO%') as temp2").on('email -> email).as(scalar[String].single)
  }



  val todasResps = {
      get[Pk[Long]]("idQrCode")~
      get[Pk[String]]("email")~
      get[String]("resposta")~
      get[Long]("pontuacao")~
      get[String]("ultima_atualizacao") map {
      case idQrCode~email~resposta~pontuacao~ultima_atualizacao => Seq(idQrCode,email,resposta,pontuacao,formataDataHora(ultima_atualizacao))
    }
  }

  def obtemTodasRespostas(id: Long) : List[Seq[Any]] = DB.withConnection { implicit c =>
    SQL("select idQrCode,email,resposta,pontuacao,ultima_atualizacao from resposta where idQrCode={id}").on('id -> id).as(todasResps *)
  }
}
