package controllers

import javax.inject._

import data.{Article, User}
import play.api.mvc._
import services.UserFinder

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}


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
  def index = Action {

    val users: Seq[User] = Await.result(userFinder.getUsers, Duration.Inf)

    val result: Seq[String] = users.map(user => s"${user.username} was born on ${user.birthday}. His/Her Email is '${user.email}'")

    Ok(views.html.index(result))
  }

}
