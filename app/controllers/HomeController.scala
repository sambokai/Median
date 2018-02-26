package controllers

import javax.inject._

import data.User
import play.api.mvc._
import services.UserFinder

import scala.concurrent.{ExecutionContext, Future}


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(userFinder: UserFinder, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index: Action[AnyContent] = Action.async {
    val users: Future[Seq[User]] = userFinder.getUsers

    val outputs: Future[Seq[String]] = users.map(_.map(user => s"${user.username} was born on ${user.birthday}. His/Her Email is '${user.email}'"))

    outputs.map(eventualOutput =>
      Ok(views.html.index(eventualOutput))
    )
  }

}
