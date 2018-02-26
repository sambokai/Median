package database

import java.sql.Date

import play.api.libs.json._
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{Query, TableQuery, Tag}

case class Article(id: Int, user: Int, body: String, created: Date)

object Article {
  implicit val articleFormat: OFormat[Article] = Json.format[Article]
}

class ArticleTable(tag: Tag) extends Table[Article](tag, "ARTICLES") {

  def id = column[Int]("article_id", O.AutoInc, O.PrimaryKey)

  def user = column[Int]("user_id")

  def body = column[String]("body")

  def created = column[Date]("created_on")

  def * = (id, user, body, created) <> ((Article.apply _).tupled, Article.unapply)
}

object ArticleTable {
  val articles: Query[ArticleTable, Article, Seq] = TableQuery[ArticleTable]
}