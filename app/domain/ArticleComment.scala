package domain

import com.github.nscala_time.time.Imports._


case class ArticleComment(id: Int, user: Int, article: Int, created: DateTime, body: String)