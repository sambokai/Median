package controllers

import javax.inject.{Inject, Singleton}

import domain.User
import dto.ArticlesSection
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Call, ControllerComponents, MessagesActionBuilder}
import services.{ArticleService, CommentService, UserService}
import views.form.PostArticleForm

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BlogController @Inject()(articleService: ArticleService, commentService: CommentService, userService: UserService, messagesActionBuilder: MessagesActionBuilder, controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(controllerComponents) with I18nSupport {

  private val pageSize: Int = 5

  def articleFeedIndex(page: Int) = Action.async { implicit request =>
    val articlePostUrl = routes.BlogController.postArticleOnFeed(page)

    val allUsers: Future[Seq[User]] = userService.getAllUsers


    val eventualArticlesSection: Future[ArticlesSection] = { //TODO: Use articleService.getLatestArticles() instead of mocking an ArticleSection
      import com.github.nscala_time.time.Imports._

      val exampleUser = domain.User(1, "Sam", Some(22))
      val exampleArticle = domain.Article(1, exampleUser, "This is an example Article for development purposes.", DateTime.now, Seq.empty[domain.Comment])

      Future.successful(ArticlesSection(Seq(exampleArticle), 1, 10))
    }

    allUsers.flatMap{users =>
      eventualArticlesSection.map(section => Ok(views.html.articleFeed(section, PostArticleForm.postArticleForm, users, articlePostUrl)))
    }
  }

  def postArticleOnFeed(page: Int) = TODO


  def articleDetailIndex(articleId: Int) = TODO

  def postCommentOnArticle(articleId: Int) = TODO

}
