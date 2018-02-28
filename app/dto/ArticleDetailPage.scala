package dto

import domain.{Article, ArticleComment}

case class ArticleDetailPage(article: Article, comments: Seq[ArticleComment])