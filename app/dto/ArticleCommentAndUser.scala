package dto

import domain.User

case class ArticleCommentAndUser(user: User, articleComment: domain.ArticleComment)