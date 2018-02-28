package controllers

import javax.inject.Inject

import play.api.mvc.{AbstractController, ControllerComponents, MessagesActionBuilder}
import services.{ArticleCommentService, UserArticleService}

import scala.concurrent.ExecutionContext

class ArticleDetailController @Inject()(articleCommentService: ArticleCommentService, messagesActionBuilder: MessagesActionBuilder, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index(articleId: Int) = TODO

  def submitComment(articleId: Int) = TODO


}
