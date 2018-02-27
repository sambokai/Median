package services

import java.sql.Timestamp
import java.time.Instant
import javax.inject.{Inject, Singleton}

import database.ArticleTable.articles
import database.UserTable.users
import domain.{Article, User}
import dto.ArticleFeedPage
import play.api.db.slick.DatabaseConfigProvider
import services.UserArticleService._
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserArticleService @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._


  /**
    * @return All registered users
    */
  def getUsers: Future[Seq[User]] = db.run(users.result)


  /**
    * @param limit  Maximum number of article/user pairs to be retrieved. Commonly used for pagination.
    * @param offset Index of article to start retrieving until `limit`. Commonly used for pagination.
    * @return Future of ArticleFeedPage dto
    */
  def getLatestArticles(limit: Int, offset: Int): Future[ArticleFeedPage] = {
    val allUserArticles = for {
      (a, u) <- articles join users on (_.user === _.id)
    } yield (a, u)

    val latestArticles = allUserArticles
      .sortBy { case (art, usr) =>
        art.created.desc
      }
      .drop(offset)
      .take(limit)
      .result

    val totalcount = allUserArticles.size.result

    val finalQuery = latestArticles zip totalcount

    val result: Future[(Seq[(Article, User)], TotalCount)] = db.run(finalQuery)

    result.map {
      case (userarticles, totalCount) => {
        val totalPages: Int = roundUp(totalCount / limit) + 1
        val currentPage: Int = offset / limit + 1

        ArticleFeedPage(
          userarticles,
          currentPage,
          totalPages
        )
      }
    }
  }

  private def roundUp(d: Double): Int = math.ceil(d).toInt


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

object UserArticleService {
  type TotalCount = Int
}
