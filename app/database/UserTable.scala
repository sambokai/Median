package database

import java.sql.Date

import domain.User
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

object UserTable {

  class UserTable(tag: Tag) extends Table[User](tag, "USERS") {

    def id = column[Int]("user_id", O.AutoInc, O.PrimaryKey, O.Unique)

    def username = column[String]("username")

    def email = column[String]("email_adress")

    def birthday = column[Date]("birthday")

    def * = (id, username, email, birthday) <> ((User.apply _).tupled, User.unapply)

  }

  val users = TableQuery[UserTable]
}
