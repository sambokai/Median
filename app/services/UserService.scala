package services

import javax.inject.{Inject, Singleton}

import com.github.nscala_time.time.Imports._
import database.tables.generated.Tables._
import domain.User
import org.joda.time.Years
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserService @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._

  def getAllUsers: Future[Seq[User]] = {
    val query = Users.result

    db.run(query).map(_.map { user =>
      val age: Option[Int] = user.birthday.map(bday => Years.yearsBetween(bday, new LocalDate()).getYears)

      User(
        user.userId,
        user.username,
        age
      )
    })
  }
}