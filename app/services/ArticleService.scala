package services

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import domain.Article
import dto.ArticlesSection
import database.tables.generated.Tables._


import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ArticleService @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  def postArticle(userId: Int, body: String): Future[Int] = ???


  // TODO: Implement getLatestArticles()
  def getLatestArticles(limit: Int, offset: Int): Future[ArticlesSection] = {
    val articles: Seq[Article] = ???
    val currentPage: Int = ???
    val totalPageCount: Int = ???

    ???
  }

  def getArticle(articleId: Int): Future[Article] = ???

}