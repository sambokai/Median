package views.form

import play.api.data.Form
import play.api.data.Forms._


case class PostCommentForm(body: String, userId: Int)

object PostCommentForm {
  val postCommentForm = Form(
    mapping(
      "body" -> text,
      "userId" -> number
    )(PostCommentForm.apply)(PostCommentForm.unapply)
  )
}



