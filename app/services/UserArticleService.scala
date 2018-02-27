package services

import java.sql.Timestamp
import java.time.Instant
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


  /**
    * @return All registered users
    */
  def getUsers: Future[Seq[User]] = db.run(users.result)


  /**
    * @param limit  Maximum number of article/user pairs to be retrieved. Commonly used for pagination.
    * @param offset Index of article to start retrieving until `limit`. Commonly used for pagination.
    * @return Article/User pair sorted by article creation date and within the range defined by `limit` and `offset`.
    */
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


  /**
    * @param userId Author of the article to be posted.
    * @param body   Text to be posted
    * @return Article id of the posted article.
    */
  def postArticle(userId: Int, body: String): Future[Int] = {
    val query = (articles returning articles.map(_.id)) += Article(0, userId, body, Timestamp.from(Instant.now))
    db.run(query)
  }

}
