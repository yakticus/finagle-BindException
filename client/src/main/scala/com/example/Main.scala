package com.example

import scala.util.Random

object Main extends App {

  println(Client.moveAlong())

  (1 to 100000).foreach {
    _ =>
      println(Client.doSomethingCool(Random.nextDouble()))
  }
}
