package controllers

import javax.inject.{Inject, Singleton}

import domain.User
import dto.ArticlesSection
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents, MessagesActionBuilder}
import services.{ArticleService, CommentService, UserService}
import views.form.PostArticleForm

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BlogController @Inject()(articleService: ArticleService, commentService: CommentService, userService: UserService, messagesActionBuilder: MessagesActionBuilder, controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(controllerComponents) with I18nSupport {

  private val pageSize: Int = 5

  def articleFeedIndex(page: Int) = Action.async { implicit request =>
    val articlePostUrl = routes.BlogController.postArticleOnFeed(page)

    val allUsers: Future[Seq[User]] = userService.getAllUsers
    val eventualArticlesSection: Future[ArticlesSection] = articleService.getLatestArticles(pageSize, (page - 1) * pageSize)

    for {
      users <- allUsers
      articleSection <- eventualArticlesSection
    } yield Ok(views.html.articleFeed(articleSection, PostArticleForm.postArticleForm, users, articlePostUrl))

  }

  def postArticleOnFeed(page: Int) = TODO


  def articleDetailIndex(articleId: Int) = TODO

  def postCommentOnArticle(articleId: Int) = TODO

}
