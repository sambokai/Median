package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc._
import services.UserArticleService
import views.form.PostArticleForm

import scala.concurrent.ExecutionContext

@Singleton
class PostArticleController @Inject()(userArticleService: UserArticleService, messagesActionBuilder: MessagesActionBuilder, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def index: Action[AnyContent] = Action.async { implicit request =>
    userArticleService.getUsers.map { users =>
      Ok(views.html.postArticle(PostArticleForm.postArticleForm, users, postUrl))
    }
  }

  private val postUrl = routes.PostArticleController.submitArticle()

  def submitArticle = Action { implicit request =>
    val formData = PostArticleForm.postArticleForm.bindFromRequest.data

    userArticleService.postArticle(formData("userId").toInt, formData("body"))


    Redirect("/feed")

  }
}
