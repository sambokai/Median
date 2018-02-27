package dto

import domain.{Article, User}

case class ArticleFeedPage(articles: Seq[(Article, User)], currentPage: Int, totalPages: Int)