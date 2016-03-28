package com.example

import java.util.Date

import akka.actor.{ActorRef, Actor}
import com.example.ProcessActor.Action

class ProcessActor(resultActorRef: ActorRef) extends Actor {

  import ProcessActor._

  def receive = {
    case Action =>
      val start = System.currentTimeMillis()
      Thread.sleep(200)
      val end = System.currentTimeMillis()
      resultActorRef ! new ResultActor.Result(reqTimeInMillis = end - start)
  }
}

object ProcessActor {
  case object Initialize
  case object Action
}