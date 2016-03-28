package com.example

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.Actor

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * FIXME: 無駄が多すぎるのでこれのせいで測定ぶっこわれるならなおす
 */
class ResultActor extends Actor {

  import ResultActor._

  var buffer: mutable.Buffer[Result] = ListBuffer()
  val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  def receive = {
    case e: Result =>
      buffer += e
    case s: Stat =>
      val now = System.currentTimeMillis() / 1000 * 1000
      buffer.partition(_.now < now) match {
        case (output, mada) =>
          buffer = mada
          output.groupBy(_.now / 1000 * 1000).toSeq.sortBy(_._1).foreach {
            case (s, b) =>
              val size = b.length
              println(Seq(
                df.format(new Date(s)),
                size.toString,
                f"${b.map(_.reqTimeInMillis).sum.toDouble / size}%.2f"
              ).mkString("\t"))
          }
      }
  }

}

object ResultActor {
  case class Result(now: Long = System.currentTimeMillis(), reqTimeInMillis: Long)
  case class Stat()
}