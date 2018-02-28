package controllers

import javax.inject.Inject

import dto.ArticleDetailPage
import play.api.mvc._
import services.ArticleCommentService

import scala.concurrent.{ExecutionContext, Future}

class ArticleDetailController @Inject()(articleCommentService: ArticleCommentService, messagesActionBuilder: MessagesActionBuilder, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index(articleId: Int): Action[AnyContent] = Action.async { implicit request =>
    val eventualArticleDetailPage: Future[ArticleDetailPage] = articleCommentService.getAllCommentsFor(articleId)

    eventualArticleDetailPage.map(articleDetailPage =>
      Ok(views.html.articleDetail(articleDetailPage))
    )
  }

  def submitComment(articleId: Int) = TODO


}
