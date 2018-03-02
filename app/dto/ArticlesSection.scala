package dto

import domain.Article

case class ArticlesSection(articles: Seq[Article], currentPage: Int, totalPages: Int)
