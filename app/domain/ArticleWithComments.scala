package domain

case class ArticleWithComments(article: Article, comments: Seq[Comment])