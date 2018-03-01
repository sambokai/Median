package dto

import database.tables.generated.Tables.{ArticleCommentsRow, UsersRow}

case class ArticleCommentAndUser(user: UsersRow, articleComment: ArticleCommentsRow)