package models

import play.api.libs.json.{Json, Reads, Writes}

/**
  * Created on 1/14/2017.
  */
case class Table(id: Option[Int], name: String, participants: Int)

object Table {
  implicit val tableWrites: Writes[Table] = Json.writes[Table]
  implicit val tableReads: Reads[Table] = Json.reads[Table]
}