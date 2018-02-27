package controllers

import javax.inject._

import domain.User
import play.api.mvc._
import services.UserArticleService

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ArticleFeedController @Inject()(userArticleService: UserArticleService, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def index: Action[AnyContent] = Action.async {
    val users: Future[Seq[User]] = userArticleService.getUsers

    val outputs: Future[Seq[String]] = users.map(_.map(user => s"${user.username} was born on ${user.birthday}. His/Her Email is '${user.email}'"))

    outputs.map(eventualOutput =>
      Ok(views.html.articleFeed(eventualOutput))
    )
  }

}
