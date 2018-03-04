package services

import javax.inject.{Inject, Singleton}

import com.github.nscala_time.time.Imports._
import com.github.tototoshi.slick.MySQLJodaSupport._
import database.tables.generated
import database.tables.generated.Tables
import database.tables.generated.Tables._
import domain.{Article, Comment, User}
import dto.ArticlesSection
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ArticleService @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  def postArticle(userId: Int, body: String): Future[Int] = {
    val query = (Articles returning Articles.map(_.articleId)) += ArticlesRow(autoIncrement, userId, body, DateTime.now)
    db.run(query)
  }

  def getLatestArticles(limit: Int, offset: Int): Future[ArticlesSection] = {

    val findArticlesQuery = Articles
      .join(Users).on(_.userId === _.userId)
      .joinLeft(ArticleComments).on(_._1.articleId === _.articleId)
      .joinLeft(Users).on(_._2.map(_.userId) === _.userId)
      .sortBy {
        case (((article, _), _), _) => article.createdOn.desc
      }
      .drop(offset)
      .take(limit)

    val result: Future[Seq[(((generated.Tables.ArticlesRow, generated.Tables.UsersRow), Option[generated.Tables.ArticleCommentsRow]), Option[generated.Tables.UsersRow])]] = db.run(findArticlesQuery.result)


    val y: Future[Seq[Article]] = mapArticlesRowsAndCommentsRows(result).map(_.sortBy(-_.created.getMillis))

    val totalPageCount: Future[Int] = db.run(Articles.size.result).map(_ / limit)
    val currentPage: Int = offset / limit + 1

    for {
      a <- y
      pcount <- totalPageCount
    } yield {
      ArticlesSection(a, currentPage, pcount)
    }
  }

  def getArticle(articleId: Int): Future[Article] = {
    val q = Articles
      .filter(_.articleId === articleId)
      .join(Users).on(_.userId === _.userId)
      .joinLeft(ArticleComments).on(_._1.articleId === _.articleId)
      .joinLeft(Users).on(_._2.map(_.userId) === _.userId)


    val result: Future[Seq[(((Tables.ArticlesRow, Tables.UsersRow), Option[Tables.ArticleCommentsRow]), Option[Tables.UsersRow])]] = db.run(q.result)

    mapArticlesRowsAndCommentsRows(result).map(_.head)
  }

  private def mapArticlesRowsAndCommentsRows(result: Future[Seq[(((Tables.ArticlesRow, Tables.UsersRow), Option[Tables.ArticleCommentsRow]), Option[Tables.UsersRow])]]): Future[Seq[Article]] = {
    val x = result.map(_.groupBy {
      case (((article, articleAuthor), comment), commentAuthor) => (article, articleAuthor)
    }.mapValues {
      _.map {
        case (((_, _), comment), commentAuthor) => for {
          c <- comment
          ca <- commentAuthor
        } yield (c, ca)
      }
    })

    x.map(_.map {
      case ((article, articleAuthor), commentAuthorPairs) => {
        val comments: Seq[Comment] = commentAuthorPairs.flatMap(_.map {
          case (comment, commentAuthor) => Comment(User(commentAuthor.userId, commentAuthor.username, commentAuthor.birthday), comment.createdOn, comment.body)
        })
        Article(article.articleId, User(articleAuthor.userId, articleAuthor.username, articleAuthor.birthday), article.body, article.createdOn, comments)
      }
    }
      .toSeq
    )
  }
}
