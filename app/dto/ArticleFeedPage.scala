package dto

import database.tables.generated.Tables.{ArticlesRow, UsersRow}

case class ArticleFeedPage(articles: Seq[(ArticlesRow, UsersRow)], currentPage: Int, totalPages: Int)