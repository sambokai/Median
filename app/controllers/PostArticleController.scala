package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents, MessagesActionBuilder}

import scala.concurrent.ExecutionContext

@Singleton
class PostArticleController @Inject()(messagesActionBuilder: MessagesActionBuilder, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index = TODO

  def submitArticle = TODO
}
