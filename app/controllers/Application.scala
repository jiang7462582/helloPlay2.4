package controllers

import javax.inject.Inject

import models.Mq
import play.api._
import play.api.mvc._



class Application @Inject() (mq:Mq) extends Controller {
  def index = Action {
      mq.send("helloWorld222")
    Ok(views.html.index("Your new application is ready."))
  }
}
