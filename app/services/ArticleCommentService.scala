package services

import javax.inject.{Inject, Singleton}

import database.ArticleCommentTable.articleComments
import domain.ArticleComment
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

  def getAllCommentsFor(articleId: Int): Future[ArticleDetailPage] = ???

  def postCommentFor(articleId: Int): Future[Int] = ???

}
