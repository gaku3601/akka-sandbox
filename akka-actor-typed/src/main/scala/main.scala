import akka.actor.typed.ActorSystem
import tutorial1.Main

object main extends App {
  val testSystem = ActorSystem(Main(), "testSystem")
  testSystem ! "start"
  testSystem ! "stop"
}
