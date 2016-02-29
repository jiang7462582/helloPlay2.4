package models

import javax.inject.{Inject, Singleton}

import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

import play.api._

/**
  * Created by jiang on 15/12/24.
  */

@Singleton
class AppStop @Inject()(lifecycle: ApplicationLifecycle,mq:Mq)  {
    lifecycle.addStopHook(() => Future.successful(GlobalApp.stop()))

}



