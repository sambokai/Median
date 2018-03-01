package dto

import domain.Article

case class ArticleDetailPage(article: Article, comments: Seq[Option[ArticleCommentAndUser]])