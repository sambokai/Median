package services

import javax.inject.{Inject, Singleton}

import database.tables.generated.Tables._
import domain.Comment
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommentService @Inject()(databaseConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = databaseConfigProvider.get[JdbcProfile]

  import dbConfig._

  def postCommentFor(comment: Comment): Future[Int] = ???
}
