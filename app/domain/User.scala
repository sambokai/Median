package domain

import java.sql.Date

import play.api.libs.json.{Json, OFormat}

case class User(id: Int, username: String, email: String, birthday: Date)

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}
