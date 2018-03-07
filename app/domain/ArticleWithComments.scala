package domain

case class ArticleWithComments(article: Article, comments: Option[Seq[Comment]])