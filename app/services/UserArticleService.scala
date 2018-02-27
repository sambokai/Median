package services

import javax.inject.{Inject, Singleton}

import database.ArticleTable.articles
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

  def getLatestArticles(limit: Int, offset: Int): Future[Seq[(Article, User)]] = {
    val userArticles = for {
      (a, u) <- articles join users on (_.user === _.id)
    } yield (a, u)

    val query = userArticles
      .sortBy(_._1.created.desc)
      .drop(offset)
      .take(limit)
      .result

    db.run(query)
  }

  def postArticle(userId: Int, body: String): Future[Int] = ???

}
