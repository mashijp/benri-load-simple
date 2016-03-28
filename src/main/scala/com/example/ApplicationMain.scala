package com.example

import akka.actor.{Props, ActorSystem}
import akka.routing.RoundRobinPool

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  val concurrent = 100
  val reqpersec = 1000
  val microsecperreq = (1000 * 1000 / reqpersec).microsecond

  val resultActor = system.actorOf(Props(new ResultActor))
  val actor = system.actorOf(RoundRobinPool(concurrent).props(Props(new ProcessActor(resultActor))))

  system.scheduler.schedule(1.second, 2.second, resultActor, ResultActor.Stat())
  system.scheduler.schedule(1.second, microsecperreq, actor, ProcessActor.Action)

  system.awaitTermination()
}