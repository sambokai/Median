package controllers

import javax.inject._

import dto.ArticleFeedPage
import play.api.mvc._
import services.UserArticleService

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ArticleFeedController @Inject()(userArticleService: UserArticleService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private val pageSize = 5


  def index(page: Int): Action[AnyContent] = Action.async { implicit request =>
    val eventualArticleFeedPage: Future[ArticleFeedPage] = userArticleService.getLatestArticles(pageSize, (page - 1) * pageSize)


    eventualArticleFeedPage.map { articleFeedPage =>
      Ok(views.html.articleFeed(articleFeedPage))
    }

  }

}
