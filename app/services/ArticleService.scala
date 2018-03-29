package services

import com.github.nscala_time.time.Imports._
import com.github.tototoshi.slick.MySQLJodaSupport._
import com.google.inject.ImplementedBy
import database.tables.generated.Tables._
import domain.{Article, ArticleWithComments, Comment, User}
import dto.ArticlesSection
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


@ImplementedBy(classOf[ArticleServiceImpl])
trait ArticleService {
  def postArticle(userId: Int, body: String): Future[Int]

  def getLatestArticles(limit: Int, offset: Int): Future[ArticlesSection]

  def getArticleWithComments(articleId: Int): Future[ArticleWithComments]
}


@Singleton
class ArticleServiceImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends ArticleService {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  override def postArticle(userId: Int, body: String): Future[Int] = {
    val query = (Articles returning Articles.map(_.articleId)) += ArticlesRow(autoIncrement, userId, body, DateTime.now)
    db.run(query)
  }

  override def getLatestArticles(limit: Int, offset: Int): Future[ArticlesSection] = {
    val totalPageCount: Future[Int] = db.run(Articles.size.result).map(_ / limit)
    val currentPage: Int = offset / limit + 1

    val q = Articles
      .sortBy(_.createdOn.desc)
      .drop(offset)
      .take(limit)
      .join(Users).on(_.userId === _.userId)
      .joinLeft(UserProfilePicture).on(_._2.userId === _.userId)


    val result = db.run(q.result)

    val articles: Future[Seq[Article]] = result.map(_.map {
      case ((article, author), authorPicture) => {
        val u = User(author.userId, author.username, author.birthday, authorPicture.map(_.src))
        Article(article.articleId, u, article.body, article.createdOn)
      }
    })

    for {
      a <- articles
      pcount <- totalPageCount
    } yield ArticlesSection(a.sortBy(-_.created.getMillis), currentPage, pcount)
  }

  override def getArticleWithComments(articleId: Int): Future[ArticleWithComments] = {
    val articleQuery =
      Articles
        .filter(_.articleId === articleId)
        .join(Users).on(_.userId === _.userId)
        .joinLeft(UserProfilePicture).on(_._2.userId === _.userId)

    val article: Future[Article] = db.run(articleQuery.result).map(_.map {
      case ((articleRow, userRow), userPicRow) => {
        val user = User(userRow.userId, userRow.username, userRow.birthday, userPicRow.map(_.src))
        Article(articleRow.articleId, user, articleRow.body, articleRow.createdOn)
      }
    }.head)

    val commentQuery =
      ArticleComments
        .filter(_.articleId === articleId)
        .joinRight(Users).on(_.userId === _.userId)
        .joinLeft(UserProfilePicture).on(_._2.userId === _.userId)
        .sortBy {
          case ((comment, _), _) => comment.map(_.createdOn)
        }


    val comments: Future[Seq[Comment]] = db.run(commentQuery.result).map(_.flatMap {
      case ((commentRow, userRow), userPicRow) => commentRow.map { comment =>
        val user = User(userRow.userId, userRow.username, userRow.birthday, userPicRow.map(_.src))
        Comment(user, comment.createdOn, comment.body)
      }
    })

    for {
      a <- article
      c <- comments
    } yield ArticleWithComments(a, c)

  }

}
