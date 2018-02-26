package database

import java.sql.Timestamp

import domain.Article
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{Query, TableQuery, Tag}

object ArticleTable {

  class ArticleTable(tag: Tag) extends Table[Article](tag, "ARTICLES") {

    def id = column[Int]("article_id", O.AutoInc, O.PrimaryKey)

    def user = column[Int]("user_id")

    def body = column[String]("body")

    def created = column[Timestamp]("created_on")

    def * = (id, user, body, created) <> ((Article.apply _).tupled, Article.unapply)
  }

  val articles: Query[ArticleTable, Article, Seq] = TableQuery[ArticleTable]
}

