package views.form

import play.api.data.Form
import play.api.data.Forms._


case class PostArticleForm(body: String, userId: Int)

object PostArticleForm {
  val postArticleForm = Form(
    mapping (
      "body" -> text,
      "userId" -> number
    )(PostArticleForm.apply)(PostArticleForm.unapply)
  )
}

