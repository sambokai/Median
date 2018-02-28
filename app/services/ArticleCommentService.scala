package services

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class ArticleCommentService @Inject()(databaseConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = databaseConfigProvider.get[JdbcProfile]

  import dbConfig._

  def getCommentsFor(articleId: Int) = ???

  def postCommentFor(articleId: Int) = ???

}
