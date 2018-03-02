package services

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import domain.{Article, ArticlesSection}

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ArticleService @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  def postArticle(userId: Int, body: String): Future[Int] = ???

  def getLatestArticles(limit: Int, offset: Int): Future[ArticlesSection] = ???

  def getArticle(articleId: Int): Future[Article] = ???

}