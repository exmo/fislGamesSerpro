package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Date

case class Palestra (
    val id: Pk[Long] = NotAssigned,
    val evento_id: Int = 1,
    var descricao: String,
    var data:String,
    var palestrante:String

)

object Palestra {

  val palestra = {
      get[Pk[Long]]("id") ~
        get[Int]("evento_id")~
        get[String]("descricao")~
        get[String]("palestrante")~
        get[String]("data") map {
        case id~evento_id~descricao~palestrante~data => Palestra(id,evento_id, descricao, palestrante, data)
      }
    }

  def all(): List[Palestra] = DB.withConnection { implicit c =>
    SQL("select * from palestra").as(palestra *)
  }

  def create(descricao: String, data: String, palestrante: String, evento_id: Int): Long = {
    DB.withConnection { implicit c =>
      SQL("insert into palestra (descricao, data, palestrante, evento_id) values ({descricao},{data},{palestrante},{evento_id})").on(
        'descricao -> descricao,
        'data -> data,
        'palestrante -> palestrante,
        'evento_id -> evento_id
      ).executeInsert()
    } match {
      case Some(insertedId) => return insertedId
      case None  => return -1
    }
  }

  def update(id:Int, descricao: String, data: String, palestrante: String, evento_id: Int) {
    DB.withConnection { implicit c =>
      SQL("update palestra set descricao = {descricao}, data = {data}, palestrante = {palestrante}, evento_id = {evento_id} where id = {id}").on(
        'id -> id,
        'descricao -> descricao,
        'data -> data,
        'palestrante -> palestrante,
        'evento_id -> evento_id
      ).executeUpdate()
    }
  }

  def findById(id: Long): Option[Palestra] = DB.withConnection { implicit c =>
    SQL("select * from palestra where id = {id}").on('id -> id).as(Palestra.palestra.singleOpt)
  }

  def delete(id: Long): Long = {
    DB.withConnection { implicit c =>
      return SQL("delete from palestra where id = {id}").on('id -> id).executeUpdate()
    }
  }

  def registrarParticipacao(idPalestra:Long, email: String, tipo: String) {
    var sdfDate = new SimpleDateFormat("dd/MM/yyyy kk:mm")
    sdfDate.setTimeZone(TimeZone.getTimeZone("GMT-3"))

    val data_hora = sdfDate.format(new Date()) // "08/08/2013 16:57"
    DB.withConnection { implicit c =>
      SQL("insert into participacao_palestra (idPalestra, email, data_hora, tipo) values ({idPalestra},{email},{data_hora},{tipo})").on(
        'idPalestra -> idPalestra,
        'email -> email,
        'data_hora -> data_hora,
        'tipo -> tipo
      ).executeInsert()
    } match {
      case Some(insertedId) => return insertedId
      case None  => return -1
    }
  }
}

