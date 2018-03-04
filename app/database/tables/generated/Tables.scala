package database.tables.generated

import com.github.nscala_time.time.Imports._
import com.github.tototoshi.slick.MySQLJodaSupport._

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile

  val autoIncrement: Int = 0
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = ArticleComments.schema ++ Articles.schema ++ Users.schema

  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table ArticleComments
    *
    * @param articleCommentId Database column article_comment_id SqlType(INT), AutoInc, PrimaryKey
    * @param userId           Database column user_id SqlType(INT)
    * @param articleId        Database column article_id SqlType(INT)
    * @param createdOn        Database column created_on SqlType(DATETIME)
    * @param body             Database column body SqlType(TEXT) */
  case class ArticleCommentsRow(articleCommentId: Int, userId: Int, articleId: Int, createdOn: DateTime, body: String)

  /** GetResult implicit for fetching ArticleCommentsRow objects using plain SQL queries */
  implicit def GetResultArticleCommentsRow(implicit e0: GR[Int], e1: GR[DateTime], e2: GR[String]): GR[ArticleCommentsRow] = GR {
    prs =>
      import prs._
      ArticleCommentsRow.tupled((<<[Int], <<[Int], <<[Int], <<[DateTime], <<[String]))
  }

  /** Table description of table ARTICLE_COMMENTS. Objects of this class serve as prototypes for rows in queries. */
  class ArticleComments(_tableTag: Tag) extends profile.api.Table[ArticleCommentsRow](_tableTag, Some("BLOGGING"), "ARTICLE_COMMENTS") {
    def * = (articleCommentId, userId, articleId, createdOn, body) <> (ArticleCommentsRow.tupled, ArticleCommentsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(articleCommentId), Rep.Some(userId), Rep.Some(articleId), Rep.Some(createdOn), Rep.Some(body)).shaped.<>({ r => import r._; _1.map(_ => ArticleCommentsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column article_comment_id SqlType(INT), AutoInc, PrimaryKey */
    val articleCommentId: Rep[Int] = column[Int]("article_comment_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column article_id SqlType(INT) */
    val articleId: Rep[Int] = column[Int]("article_id")
    /** Database column created_on SqlType(DATETIME) */
    val createdOn: Rep[DateTime] = column[DateTime]("created_on")
    /** Database column body SqlType(TEXT) */
    val body: Rep[String] = column[String]("body")

    /** Foreign key referencing Articles (database name ARTICLE_COMMENTS_ARTICLES_article_id_fk) */
    lazy val articlesFk = foreignKey("ARTICLE_COMMENTS_ARTICLES_article_id_fk", articleId, Articles)(r => r.articleId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name ARTICLE_COMMENTS_USERS_user_id_fk) */
    lazy val usersFk = foreignKey("ARTICLE_COMMENTS_USERS_user_id_fk", userId, Users)(r => r.userId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  /** Collection-like TableQuery object for table ArticleComments */
  lazy val ArticleComments = new TableQuery(tag => new ArticleComments(tag))

  /** Entity class storing rows of table Articles
    *
    * @param articleId Database column article_id SqlType(INT), AutoInc, PrimaryKey
    * @param userId    Database column user_id SqlType(INT)
    * @param body      Database column body SqlType(VARCHAR), Length(500,true)
    * @param createdOn Database column created_on SqlType(DATETIME) */
  case class ArticlesRow(articleId: Int, userId: Int, body: String, createdOn: DateTime)

  /** GetResult implicit for fetching ArticlesRow objects using plain SQL queries */
  implicit def GetResultArticlesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[DateTime]): GR[ArticlesRow] = GR {
    prs =>
      import prs._
      ArticlesRow.tupled((<<[Int], <<[Int], <<[String], <<[DateTime]))
  }

  /** Table description of table ARTICLES. Objects of this class serve as prototypes for rows in queries. */
  class Articles(_tableTag: Tag) extends profile.api.Table[ArticlesRow](_tableTag, Some("BLOGGING"), "ARTICLES") {
    def * = (articleId, userId, body, createdOn) <> (ArticlesRow.tupled, ArticlesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(articleId), Rep.Some(userId), Rep.Some(body), Rep.Some(createdOn)).shaped.<>({ r => import r._; _1.map(_ => ArticlesRow.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column article_id SqlType(INT), AutoInc, PrimaryKey */
    val articleId: Rep[Int] = column[Int]("article_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column body SqlType(VARCHAR), Length(500,true) */
    val body: Rep[String] = column[String]("body", O.Length(500, varying = true))
    /** Database column created_on SqlType(DATETIME) */
    val createdOn: Rep[DateTime] = column[DateTime]("created_on")

    /** Foreign key referencing Users (database name ARTICLES_USERS_user_id_fk) */
    lazy val usersFk = foreignKey("ARTICLES_USERS_user_id_fk", userId, Users)(r => r.userId, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  /** Collection-like TableQuery object for table Articles */
  lazy val Articles = new TableQuery(tag => new Articles(tag))

  /** Entity class storing rows of table Users
    *
    * @param userId      Database column user_id SqlType(INT), AutoInc, PrimaryKey
    * @param username    Database column username SqlType(TEXT)
    * @param emailAdress Database column email_adress SqlType(TEXT)
    * @param birthday    Database column birthday SqlType(DATE), Default(None) */
  case class UsersRow(userId: Int, username: String, emailAdress: String, birthday: Option[LocalDate] = None)

  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[LocalDate]]): GR[UsersRow] = GR {
    prs =>
      import prs._
      UsersRow.tupled((<<[Int], <<[String], <<[String], <<?[LocalDate]))
  }

  /** Table description of table USERS. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, Some("BLOGGING"), "USERS") {
    def * = (userId, username, emailAdress, birthday) <> (UsersRow.tupled, UsersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(username), Rep.Some(emailAdress), birthday).shaped.<>({ r => import r._; _1.map(_ => UsersRow.tupled((_1.get, _2.get, _3.get, _4))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(INT), AutoInc, PrimaryKey */
    val userId: Rep[Int] = column[Int]("user_id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(TEXT) */
    val username: Rep[String] = column[String]("username")
    /** Database column email_adress SqlType(TEXT) */
    val emailAdress: Rep[String] = column[String]("email_adress")
    /** Database column birthday SqlType(DATE), Default(None) */
    val birthday: Rep[Option[LocalDate]] = column[Option[LocalDate]]("birthday", O.Default(None))
  }

  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}

