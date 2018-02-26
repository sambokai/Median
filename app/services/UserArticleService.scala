package services

import javax.inject.{Inject, Singleton}

import database.UserTable.users
import domain.{Article, User}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future


@Singleton
class UserArticleService @Inject()(dbConfigProvider: DatabaseConfigProvider) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  def getUsers: Future[Seq[User]] = db.run(users.result)

  def getUserArticles(from: Int, to: Int): Future[Map[User, Seq[Article]]] = ???

  def postArticle(userId: Int, body: String): Future[Int] = ???

}
