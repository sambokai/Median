package domain

import org.scalatestplus.play._
import com.github.nscala_time.time.Imports._

class UserSpec extends PlaySpec {

  "A User" must {
    "provide the age in years, given the birthday" in {
      val birthday = LocalDate.now.minusYears(10)
      val user = User(1, "John Doe", Some(birthday), None)

      user.age mustBe Some(10)
    }
  }

}
