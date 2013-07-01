package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import util.Random

case class QRCode (
  val id: Pk[Long] = NotAssigned,
  var texto:String,
  var tipo:String,
  var resposta:String,
  var alternativa1:String,
  var alternativa2:String,
  var alternativa3:String,
  var textoQrCode:String,
  var pontuacao:Int
)

object QRCode {

  val qrcode = {
    get[Pk[Long]]("id") ~
      get[String]("texto")~
      get[String]("tipo")~
      get[String]("resposta") ~
      get[String]("alternativa1") ~
      get[String]("alternativa2") ~
      get[String]("alternativa3") ~
      get[String]("textoQrCode") ~
      get[Int]("pontuacao") map {
      case id~texto~tipo~resposta~alternativa1~alternativa2~alternativa3~textoQrCode~pontuacao => QRCode(id, texto, tipo, resposta, alternativa1, alternativa2, alternativa3, textoQrCode, pontuacao)
    }
  }

  def all(): List[QRCode] = DB.withConnection { implicit c =>
    SQL("select * from qrcode").as(qrcode *)
  }

  def findById(id: Long): Option[QRCode] = DB.withConnection { implicit c =>
    SQL("select * from qrcode where id={id}").on('id -> id).as(QRCode.qrcode.singleOpt)
  }

  def criaTextoQrCode(texto: String, tipo: String, resposta:String, alternativa1:String, alternativa2:String, alternativa3:String, pontuacao:Int): String = {

        var random = Random.nextInt(4);
        var textoQrCode = texto+"#"+pontuacao;
        if(tipo == "DESAFIOME"){
            textoQrCode = textoQrCode + "#" + alternativa1 + "#" +  alternativa2 + "#" + alternativa3
        }
        return textoQrCode
  }


  def create(texto: String, tipo: String, resposta:String, alternativa1:String, alternativa2:String, alternativa3:String, pontuacao:Int) {
    val textoQrCode = criaTextoQrCode(texto,tipo,resposta,alternativa1,alternativa2,alternativa3,pontuacao);
    DB.withConnection { implicit c =>
      SQL("insert into qrcode (texto,tipo,resposta,alternativa1,alternativa2,alternativa3,textoQrCode,pontuacao) values ({texto},{tipo},{resposta},{alternativa1},{alternativa2},{alternativa3},{textoQrCode},{pontuacao})").on(
        'texto -> texto,
        'tipo -> tipo,
        'resposta -> resposta,
        'alternativa1 -> alternativa1,
        'alternativa2 -> alternativa2,
        'alternativa3 -> alternativa3,
        'textoQrCode -> textoQrCode,
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
