package services

import javax.inject.{Inject, Singleton}

import database.ArticleCommentTable.articleComments
import database.ArticleTable.articles
import database.UserTable.users
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

  def getAllComments: Future[Seq[ArticleComment]] = db.run(articleComments.result)

  def getAllCommentsFor(articleId: Int): Future[ArticleDetailPage] = {

    val commentArticleUserPairs = for {
      ((article, comment), user) <- articles joinLeft articleComments on (_.id === _.article) joinLeft users on (_._2.map(_.user) === _.id)
    } yield (article, comment, user)

    val commentArticleUserPairsForWantedArticleId = commentArticleUserPairs.filter {
      case (article, _, _) => article.id === articleId
    }

    val resultingRows: Future[Seq[(Article, Option[ArticleComment], Option[User])]] = db.run(commentArticleUserPairsForWantedArticleId.result)


    val resultingArticleMap: Future[Map[Article, Seq[(Option[ArticleComment], Option[User])]]] = resultingRows.map { eventualResult =>
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
