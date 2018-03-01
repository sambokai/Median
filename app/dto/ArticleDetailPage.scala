package dto

import database.tables.generated.Tables.ArticlesRow

case class ArticleDetailPage(article: ArticlesRow, comments: Seq[Option[ArticleCommentAndUser]])