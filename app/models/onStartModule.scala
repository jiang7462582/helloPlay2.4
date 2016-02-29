package models

import com.google.inject.AbstractModule


/**
  * Created by jiang on 15/12/24.
  */
class onStartModule extends AbstractModule{
    override def configure() = {

        bind(classOf[AppStop]).asEagerSingleton()
        bind(classOf[GlobalApp]).asEagerSingleton()
    }

}
