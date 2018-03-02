package domain

import com.github.nscala_time.time.Imports._

case class Article(id: Int, user: User, body: String, created: DateTime, comments: Seq[Comment])