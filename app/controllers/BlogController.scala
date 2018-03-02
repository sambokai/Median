package controllers

import javax.inject.{Inject, Singleton}

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Call, ControllerComponents, MessagesActionBuilder}
import services.{ArticleService, CommentService}

@Singleton
class BlogController @Inject()(articleService: ArticleService, commentService: CommentService, messagesActionBuilder: MessagesActionBuilder, controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) with I18nSupport {

  def articleFeedIndex(page: Int) = TODO

  def postArticleOnFeed(page: Int) = TODO


  def articleDetailIndex(articleId: Int) = TODO

  def postCommentOnArticle(articleId: Int) = TODO

}
