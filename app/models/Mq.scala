package models

/**
  * Mq Utills
  *
  */

import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.Session
import javax.jms.TextMessage

import javax.inject.{Inject, Singleton}

import scala.language.postfixOps
import scala.collection.mutable.ListBuffer
import play.api.Configuration

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.pool.PooledConnectionFactory



@Singleton
class Mq @ Inject()(configuration: Configuration) {

    val activeMQServerIPAddr = configuration.getString("activemq.server.ip").getOrElse("")
    val activeMQServerUser = configuration.getString("activemq.server.user").getOrElse("")
    val activeMQServerPasswd = configuration.getString("activemq.server.passwd").getOrElse("")
    val factory: ActiveMQConnectionFactory = new ActiveMQConnectionFactory(activeMQServerIPAddr)
    val pooledMaxConnectionSize: Int = 20
    val pooledFactory: PooledConnectionFactory = new PooledConnectionFactory(factory)
    pooledFactory.setMaxConnections(pooledMaxConnectionSize)
    val queueName = "helloPlay2.4"

    def send(textMsg: String) = {
        val connection = pooledFactory.createConnection(activeMQServerUser, activeMQServerPasswd)
        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val destination = session.createQueue(queueName)
        val producer = session.createProducer(destination)
        producer.send(session.createTextMessage(textMsg))
        session.close()
    }

    def recv: Session = {
        val cf = new ActiveMQConnectionFactory(activeMQServerIPAddr)
        cf.setCopyMessageOnSend(false)
        val connection = cf.createConnection()
        connection.start()
        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val destination = session.createQueue(queueName)
        val consumer = session.createConsumer(destination)
        val listener = new MessageListener {
            def onMessage(message: Message) {
                message match {
                    case text: TextMessage => {
                        // consumer message via ws
                        val textContentComp = text.getText
                        // decompress the text msg received

                        println(textContentComp)
                    }
                    case _ => {
                        throw new Exception("Unhandled message type: " + message.getClass.getSimpleName)
                    }
                }
            }
        }
        consumer.setMessageListener(listener)
        session
    }

}


class GlobalApp @ Inject()(mq:Mq) {
    val consumers = GlobalApp.consumers
    consumers += mq.recv

}
object GlobalApp{
    lazy val consumers = new ListBuffer[Session]()
    def stop() = {
        consumers.toList.foreach((session: Session) => {
            session.close()
        })
    }


}

