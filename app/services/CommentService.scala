package services

import javax.inject.{Inject, Singleton}

import com.github.nscala_time.time.Imports._
import database.tables.generated.Tables._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CommentService @Inject()(databaseConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = databaseConfigProvider.get[JdbcProfile]

  import dbConfig._

  def postCommentFor(userId: Int, articleId: Int, body: String): Future[Int] = {
    val query = (ArticleComments returning ArticleComments.map(_.articleCommentId)) += ArticleCommentsRow(autoIncrement, userId = userId, articleId = articleId, DateTime.now, body = body)
    db.run(query)
  }
}
