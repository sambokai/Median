package controllers

import javax.inject._

import play.api.mvc._
import services.UserArticleService

import scala.concurrent.ExecutionContext


@Singleton
class ArticleFeedController @Inject()(userArticleService: UserArticleService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private val pageSize = 5

  def index: Action[AnyContent] = Action.async {
    val articles = userArticleService.getLatestArticles(pageSize, 0)

    articles.map { eventualArticles =>
      Ok(views.html.articleFeed(eventualArticles))
    }

  }

}
