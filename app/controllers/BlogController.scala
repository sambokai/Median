package controllers

import javax.inject.{Inject, Singleton}

import domain.User
import dto.ArticlesSection
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{ArticleService, CommentService, UserService}
import views.form.PostArticleForm

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BlogController @Inject()(articleService: ArticleService, commentService: CommentService, userService: UserService, messagesActionBuilder: MessagesActionBuilder, controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(controllerComponents) with I18nSupport {

  private val pageSize: Int = 5

  def articlePostUrl(page: Int) = routes.BlogController.postArticleOnFeed(page)


  def serveFeed(page: Int, pageSize: Int)(implicit request: Request[AnyContent]) = {
    val allUsers: Future[Seq[User]] = userService.getAllUsers
    val eventualArticlesSection: Future[ArticlesSection] = articleService.getLatestArticles(pageSize, (page - 1) * pageSize)

    for {
      users <- allUsers
      articleSection <- eventualArticlesSection
    } yield Ok(views.html.articleFeed(articleSection, PostArticleForm.postArticleForm, users, articlePostUrl(page)))
  }

  def articleFeedIndex(page: Int) = Action.async { implicit request => this.serveFeed(page, pageSize) }

  def postArticleOnFeed(page: Int) = Action.async { implicit request =>
    val formData = PostArticleForm.postArticleForm.bindFromRequest.data
    articleService.postArticle(formData("userId").toInt, formData("body"))

    this.serveFeed(page, pageSize)
  }


  def articleDetailIndex(articleId: Int) = Action.async { implicit request =>
    articleService.getArticle(articleId).map(article => Ok(views.html.articleDetail(article)))
  }

  def postCommentOnArticle(articleId: Int) = TODO

  def userDetailIndex(userId: Int) = TODO

}
