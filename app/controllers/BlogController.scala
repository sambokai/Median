package controllers

import javax.inject.{Inject, Singleton}
import domain.{ArticleWithComments, User}
import dto.ArticlesSection
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{ArticleService, CommentService, UserService}
import views.form.{PostArticleForm, PostCommentForm}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BlogController @Inject()(articleService: ArticleService, commentService: CommentService, userService: UserService, messagesActionBuilder: MessagesActionBuilder, controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(controllerComponents) with I18nSupport {

  private val pageSize: Int = 5


  private def articlePostUrl(page: Int) = routes.BlogController.postArticleOnFeed(page)

  private def serveFeed(page: Int, pageSize: Int)(implicit request: Request[AnyContent]) = {
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

  private def commentPostUrl(articleId: Int) = routes.BlogController.postCommentOnArticle(articleId)

  private def serveArticleDetail(articleId: Int)(implicit request: Request[AnyContent]) = {
    val allUsers: Future[Seq[User]] = userService.getAllUsers
    val eventualArticle: Future[ArticleWithComments] = articleService.getArticleWithComments(articleId)

    for {
      users <- allUsers
      article <- eventualArticle
    } yield Ok(views.html.articleDetail(article, PostCommentForm.postCommentForm, users, commentPostUrl(articleId)))
  }

  def articleDetailIndex(articleId: Int) = Action.async { implicit request =>
    serveArticleDetail(articleId)
  }

  def postCommentOnArticle(articleId: Int) = Action.async { implicit request =>
    val formData = PostCommentForm.postCommentForm.bindFromRequest.data
    commentService.postCommentFor(formData("userId").toInt, articleId, formData("body"))

    serveArticleDetail(articleId)
  }


  def userDetailIndex(userId: Int) = Action.async { implicit request =>
    userService.getUser(userId).map(user => Ok(views.html.userDetail(user)))
  }

}
