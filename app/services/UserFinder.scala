package services

import javax.inject.{Inject, Singleton}

import data.User
import data.UserTable.users
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future


@Singleton
class UserFinder @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  def getUsers: Future[Seq[User]] = db.run(users.result)

}
