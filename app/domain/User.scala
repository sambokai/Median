package domain

import com.github.nscala_time.time.Imports._
import org.joda.time.Years

case class User(id: Int, username: String, birthday: Option[LocalDate], pictureSource: Option[String]) {

  def age: Option[Int] = birthday.map(bday => Years.yearsBetween(bday, LocalDate.now).getYears)

}