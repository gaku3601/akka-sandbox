import akka.actor.typed.ActorSystem
import tutorial1.EntryPoint

object Main extends App {
  val testSystem = ActorSystem(EntryPoint(), "testSystem")
  testSystem ! "start"
  testSystem ! "stop"
}
