package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import scala.util.Random
import java.math.BigInteger
import scala._
import anorm.~
import anorm.Id
import scala.Some
import anorm.~
import anorm.Id
import scala.Some

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._


case class QRCode (
  val id: Pk[Long] = NotAssigned,
  val evento_id: Int = 1,
  var texto:String,
  var tipo:String,
  var resposta:String,
  var alternativas:String,
  var pontuacao:Int

)

object QRCode {

  implicit var anormLongPkFormat = new Format[Pk[Long]] {
    def writes(key: Pk[Long]): JsValue = Json.toJson(key.toString)
    def reads(jv: JsValue): JsResult[Pk[Long]] =   JsError()
  }

  implicit val qrCodeWrites: Writes[QRCode] = (
      (__ \ "id").write[Pk[Long]] and
      (__ \ "evento_id").write[Int] and
      (__ \ "texto").write[String] and
      (__ \ "tipo").write[String] and
      (__ \ "resposta").write[String] and
      (__ \ "alternativas").write[String] and
      (__ \ "pontuacao").write[Int]
  )(unlift(QRCode.unapply))

  val qrcode = {
    get[Pk[Long]]("id") ~
      get[Int]("evento_id")~
      get[String]("texto")~
      get[String]("tipo")~
      get[String]("resposta") ~
      get[String]("alternativas") ~
      get[Int]("pontuacao") map {
      case id~evento_id~texto~tipo~resposta~alternativas~pontuacao => QRCode(id,evento_id, texto, tipo, resposta, alternativas, pontuacao)
    }
  }

  def all(): List[QRCode] = DB.withConnection { implicit c =>
    SQL("select * from qrcode").as(qrcode *)
  }

  def findById(id: Long): Option[QRCode] = DB.withConnection { implicit c =>
    SQL("select * from qrcode where id={id}").on('id -> id).as(QRCode.qrcode.singleOpt)
  }

  def create(evento_id: Int, texto: String, tipo: String, resposta:String, alternativas:String, pontuacao:Int): Long = {
    DB.withConnection { implicit c =>
      SQL("insert into qrcode (evento_id,texto,tipo,resposta,alternativas,pontuacao) values ({evento_id},{texto},{tipo},{resposta},{alternativas},{pontuacao})").on(
        'evento_id -> evento_id,
        'texto -> texto,
        'tipo -> tipo,
        'resposta -> resposta,
        'alternativas -> alternativas,
        'pontuacao -> pontuacao
      ).executeInsert()
    } match {
      case Some(insertedId) => return insertedId
      case None  => return -1
    }
  }

  def delete(id: Long): Long = {
    DB.withConnection { implicit c =>
      return SQL("delete from qrcode where id = {id}").on('id -> id).executeUpdate()
    }
  }

//
//  implicit object QRCodeFormat extends Format[QRCode] {
//
//
//    def writes(q: QRCode): JsValue = JsObject(
//        Seq(
//        "id" -> JsNumber(q.id.get),
//        "evento_id" -> JsNumber(q.evento_id),
//        "tipo" -> JsString(q.tipo),
//        "texto" -> JsString(q.texto),
//        "textoQrCode" -> JsString(q.textoQrCode),
//        "resposta" -> JsString(q.resposta),
//        "alternativas" -> JsString(q.alternativas),
//        "pontuacao" -> JsNumber(q.pontuacao)
//        )
//    )
//
//
//    def reads(json: JsValue): QRCode =
//      QRCode(
//        Id[Long]((json \ "id").as[Long]),
//        (json \ "evento_id").as[Int],
//        (json \ "tipo").as[String],
//        (json \ "texto").as[String],
//        (json \ "textoQrCode").as[String],
//        (json \ "resposta").as[String],
//        (json \ "alternativas").as[String],
//        (json \ "pontuacao").as[Int]
//      )
//
//  }


}
