package controllers

import javax.inject.Inject

import models.Mq
import play.api._
import play.api.libs.ws.WSClient
import play.api.mvc._



class Application @Inject() (ws: WSClient,mq:Mq) extends Controller {

  def index = Action {

      //val s = new MyComponent(ws)(1)
      mq.send("helloWorld222")

    Ok(views.html.index("Your new application is ready."))
  }

}




class MyComponent @Inject() (ws: WSClient)(x:Int) {
    def s = ws.url("www.baidu.com").get()
    // ...
}