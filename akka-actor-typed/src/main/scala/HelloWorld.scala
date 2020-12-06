import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Behavior, PostStop, Signal}

object HelloWorld extends App {
  val testSystem = ActorSystem(Main(), "testSystem")
  testSystem ! "start"
  testSystem ! "stop"
}

object Main {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new Main(context))
}

class Main(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "start" =>
        val firstRef = context.spawn(PrintMyActorRefActor(), "first-actor") // actorを生成する
        println(s"First: $firstRef")
        firstRef ! "printit"
        this
      case "stop" => Behaviors.stopped // actorを停止する
    }

  override def onSignal: PartialFunction[Signal, Behavior[String]] = {
    // actor 終了時の処理
    case PostStop =>
      println("first stop")
      this
  }
}

object PrintMyActorRefActor {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new PrintMyActorRefActor(context))
}

class PrintMyActorRefActor(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "printit" =>
        val secondRef = context.spawn(Behaviors.empty[String], "second-actor")
        println(s"Second: $secondRef")
        this
    }

  override def onSignal: PartialFunction[Signal, Behavior[String]] = {
    case PostStop =>
      println("second stop")
      this
  }
}