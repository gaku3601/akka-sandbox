package tutorial1

import akka.actor.typed._
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object EntryPoint {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new EntryPoint(context))
}

class EntryPoint(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "start" =>
        val firstRef = context.spawn(PrintMyActorRefActor(), "first-actor") // actorを生成する
        // 子actorで例外が出力された場合、restartする
        val supervisedRef = context.spawn(Behaviors.supervise(SupervisedActor()).onFailure(SupervisorStrategy.restart), "supervised-actor")
        println(s"First: $firstRef")
        firstRef ! "printit"
        supervisedRef ! "fails"
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

object SupervisedActor {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new SupervisedActor(context))
}

class SupervisedActor(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "fails" =>
        println("fails!")
        throw new Exception("throw exception!")
    }

  override def onSignal: PartialFunction[Signal, Behavior[String]] = {
    case PreRestart =>
      println("restart!")
      this
    case PostStop =>
      println("stop!")
      this
  }
}