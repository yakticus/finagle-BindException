package com.example

import com.twitter.{finagle, util => twitter}

object Client  {
  private[this] val client: finagle.Service[finagle.http.Request, finagle.http.Response] =
    finagle.Http.newService("localhost:5000")

  def moveAlong(): String = {
    getResponseBody("/")
  }

  def doSomethingCool(thing: Double): String = {
    postJson("/doSomethingCool", "{\"thing\": " + thing + "}")
  }

  private def postJson(uri: String, json: String): String = {
    val request = finagle.http.Request(finagle.http.Method.Post, uri)
    request.contentType = finagle.http.MediaType.Json
    request.contentString = json
    awaitString(client(request))
  }

  private def getResponseBody(uri: String) = {
    val request = finagle.http.Request(finagle.http.Method.Get, uri)
    awaitString(client(request))
  }

  private def awaitString(response: twitter.Future[finagle.http.Response]): String = {
    // just block and wait for the response
    val awaited = twitter.Await.result(response)
    awaited.getContentString()
  }

}
