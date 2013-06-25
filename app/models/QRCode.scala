package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
//import java.sql._
//import org.joda.time._
//import java.math.BigInteger
//import org.joda.time.DateTime

case class QRCode (

  val id: Pk[Long] = NotAssigned,
  var texto:String,
  var tipo:String,
  var resposta:String,
  var pontuacao:Int
)

object QRCode {

  val qrcode = {
    get[Pk[Long]]("id") ~
      get[String]("texto")~
      get[String]("tipo")~
      get[String]("resposta") ~
      get[Int]("pontuacao") map {
      case id~texto~tipo~resposta~pontuacao => QRCode(id, texto, tipo, resposta, pontuacao)
    }
  }

  def all(): List[QRCode] = DB.withConnection { implicit c =>
    SQL("select * from qrcode").as(qrcode *)
  }

  def findById(id: Long): Option[QRCode] = DB.withConnection { implicit c =>
    SQL("select * from qrcode where id={id}").on('id -> id).as(QRCode.qrcode.singleOpt)
  }

  def create(texto: String, tipo: String, resposta:String, pontuacao:Int) {
    DB.withConnection { implicit c =>
      SQL("insert into qrcode (texto,tipo,resposta,pontuacao) values ({texto},{tipo},{resposta},{pontuacao})").on(
        'texto -> texto,
        'tipo -> tipo,
        'resposta -> resposta,
        'pontuacao -> pontuacao
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from qrcode where id = {id}").on('id -> id).executeUpdate()
    }
  }

}