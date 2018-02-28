package database

import com.github.nscala_time.time.Imports._
import com.github.tototoshi.slick.MySQLJodaSupport._
import domain.ArticleComment
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}


object ArticleCommentTable {

  class ArticleCommentTable(tag: Tag) extends Table[ArticleComment](tag, "ARTICLE_COMMENTS") {

    def id = column[Int]("article_comment_id", O.AutoInc, O.PrimaryKey)

    def user = column[Int]("user_id")

    def article = column[Int]("article_id")

    def created = column[DateTime]("created_on")

    def body = column[String]("body")

    def * = (id, user, article, created, body) <> ((ArticleComment.apply _).tupled, ArticleComment.unapply)
  }

  val articleComments = TableQuery[ArticleCommentTable]
}
