package services

import javax.inject.{Inject, Singleton}

import database.tables.generated
import database.tables.generated.Tables
import database.tables.generated.Tables._
import domain.{Article, ArticleComment, User}
import dto.ArticleDetailPage
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ArticleCommentService @Inject()(databaseConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = databaseConfigProvider.get[JdbcProfile]

  import dbConfig._

  def getAllComments: Future[Seq[ArticleCommentsRow]] = db.run(ArticleComments.result)

  def getAllCommentsFor(articleId: Int): Future[ArticleDetailPage] = {

    val commentArticleUserPairs = for {
      ((article, comment), user) <- Articles joinLeft ArticleComments on (_.articleId === _.articleId) joinLeft Users on (_._2.map(_.userId) === _.userId)
    } yield (article, comment, user)

    val commentArticleUserPairsForWantedArticleId = commentArticleUserPairs.filter {
      case (article, _, _) => article.articleId === articleId
    }

    val resultingRows: Future[Seq[(ArticlesRow, Option[ArticleCommentsRow], Option[UsersRow])]] = db.run(commentArticleUserPairsForWantedArticleId.result)


    val resultingArticleMap: Future[Map[ArticlesRow, Seq[(Option[ArticleCommentsRow], Option[UsersRow])]]] = resultingRows.map { eventualResult =>
      eventualResult.groupBy {
        case (article, _, _) => article
      }.mapValues(_.map {
        case (_, comment, user) => (comment, user)
      })
    }

    resultingArticleMap.map {
      _.map {
        case (article, comments) => ArticleDetailPage(article, comments.map {
          case (opComment, opUser) => for {
            c <- opComment
            u <- opUser
          } yield dto.ArticleCommentAndUser(u, c)
        })
      }.head
    }
  }

  def postCommentFor(articleId: Int): Future[Int] = ???

}
