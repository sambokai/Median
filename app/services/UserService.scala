package services

import com.google.inject.ImplementedBy
import database.tables.generated.Tables._
import domain.User
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[UserServiceImpl])
trait UserService {
  def getAllUsers: Future[Seq[User]]

  def getUser(userId: Int): Future[User]
}

@Singleton
class UserServiceImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends UserService {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  def getAllUsers: Future[Seq[User]] = {
    val query = Users
      .joinLeft(UserProfilePicture).on(_.userId === _.userId)
      .result

    db.run(query).map(_.map {
      case (user, pic) =>
        User(
          user.userId,
          user.username,
          user.birthday,
          pic.map(_.src)
        )
    })
  }

  def getUser(userId: Int): Future[User] = {
    val query =
      Users
        .filter(_.userId === userId)
        .joinLeft(UserProfilePicture).on(_.userId === _.userId)
        .result

    db.run(query).map(_.map {
      case (user, pic) =>
        User(
          user.userId,
          user.username,
          user.birthday,
          pic.map(_.src)
        )
    }.head)
  }
}