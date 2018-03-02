package domain

import com.github.nscala_time.time.Imports._

case class Comment(user: User, created: DateTime, body: String)