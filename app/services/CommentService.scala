package services

import javax.inject.{Inject, Singleton}

import com.github.nscala_time.time.Imports._
import com.google.inject.ImplementedBy
import database.tables.generated.Tables._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[CommentServiceImpl])
trait CommentService {
  def postCommentFor(userId: Int, articleId: Int, body: String): Future[Int]
}

@Singleton
class CommentServiceImpl @Inject()(databaseConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends CommentService {
  private val dbConfig = databaseConfigProvider.get[JdbcProfile]

  import dbConfig._

  override def postCommentFor(userId: Int, articleId: Int, body: String): Future[Int] = {
    val query = (ArticleComments returning ArticleComments.map(_.articleCommentId)) += ArticleCommentsRow(autoIncrement, userId = userId, articleId = articleId, DateTime.now, body = body)
    db.run(query)
  }
}
