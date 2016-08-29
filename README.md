## what is this?

This repo reproduces an exception seen when using finagle client for the purposes of diagnosis.

## install

You need Python & Pip installed to run this example. I used Python 2.7.10 and pip 8.1.2 on Mac OSX 10.11.6

`pushd server/ && pip install -r requirements.txt && popd`

## run the example

```
$ python server/server.py &
$ cd client/ && sbt run
```

## the bug

Eventually, you'll get an exception that looks like this (may take many thousands of requests)

```
com.twitter.finagle.ChannelWriteException: org.jboss.netty.channel.ConnectTimeoutException: connection timed out: localhost/127.0.0.1:5000. Remote Info: Upstream Address: Not Available, Upstream Client Id: Not Available, Downstream Address: localhost/127.0.0.1:5000, Downstream Client Id: localhost:5000, Trace Id: 869bb24b65256eeb.869bb24b65256eeb<:869bb24b65256eeb
	at org.jboss.netty.channel.socket.nio.NioClientBoss.processConnectTimeout(NioClientBoss.java:139)
	at org.jboss.netty.channel.socket.nio.NioClientBoss.process(NioClientBoss.java:83)
	at org.jboss.netty.channel.socket.nio.AbstractNioSelector.run(AbstractNioSelector.java:337)
	at org.jboss.netty.channel.socket.nio.NioClientBoss.run(NioClientBoss.java:42)
	at org.jboss.netty.util.ThreadRenamingRunnable.run(ThreadRenamingRunnable.java:108)
	at org.jboss.netty.util.internal.DeadLockProofWorker$1.run(DeadLockProofWorker.java:42)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at com.twitter.finagle.util.ProxyThreadFactory$$anonfun$newProxiedRunnable$1$$anon$1.run(ProxyThreadFactory.scala:19)
	at java.lang.Thread.run(Thread.java:745)
Caused by: org.jboss.netty.channel.ConnectTimeoutException: connection timed out: localhost/127.0.0.1:5000
	at org.jboss.netty.channel.socket.nio.NioClientBoss.processConnectTimeout(NioClientBoss.java:139)
	at org.jboss.netty.channel.socket.nio.NioClientBoss.process(NioClientBoss.java:83)
	at org.jboss.netty.channel.socket.nio.AbstractNioSelector.run(AbstractNioSelector.java:337)
	at org.jboss.netty.channel.socket.nio.NioClientBoss.run(NioClientBoss.java:42)
	at org.jboss.netty.util.ThreadRenamingRunnable.run(ThreadRenamingRunnable.java:108)
	at org.jboss.netty.util.internal.DeadLockProofWorker$1.run(DeadLockProofWorker.java:42)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at com.twitter.finagle.util.ProxyThreadFactory$$anonfun$newProxiedRunnable$1$$anon$1.run(ProxyThreadFactory.scala:19)
	at java.lang.Thread.run(Thread.java:745)
```

This is the exception I have seen in the wild, but haven't been able to see it with this reproducer.

```
Exception in thread "main" com.twitter.finagle.ChannelWriteException: java.net.BindException: Cannot assign requested address. Remote Info: Upstream Address: Not Available, Upstream Client Id: Not Available, Downstream Address: gym-server/172.17.0.3:5000, Downstream Client Id: gym-server:5000, Trace Id: bf268015585332a9.bf268015585332a9<:bf268015585332a9
Caused by: java.net.BindException: Cannot assign requested address
	at sun.nio.ch.Net.connect0(Native Method)
	at sun.nio.ch.Net.connect(Net.java:454)
	at sun.nio.ch.Net.connect(Net.java:446)
	at sun.nio.ch.SocketChannelImpl.connect(SocketChannelImpl.java:648)
	at org.jboss.netty.channel.socket.nio.NioClientSocketPipelineSink.connect(NioClientSocketPipelineSink.java:108)
	at org.jboss.netty.channel.socket.nio.NioClientSocketPipelineSink.eventSunk(NioClientSocketPipelineSink.java:70)
	at org.jboss.netty.channel.DefaultChannelPipeline$DefaultChannelHandlerContext.sendDownstream(DefaultChannelPipeline.java:779)
	at org.jboss.netty.channel.SimpleChannelHandler.connectRequested(SimpleChannelHandler.java:306)
	at org.jboss.netty.channel.SimpleChannelHandler.handleDownstream(SimpleChannelHandler.java:272)
	at org.jboss.netty.channel.DefaultChannelPipeline.sendDownstream(DefaultChannelPipeline.java:591)
	at org.jboss.netty.channel.DefaultChannelPipeline$DefaultChannelHandlerContext.sendDownstream(DefaultChannelPipeline.java:784)
	at org.jboss.netty.channel.SimpleChannelHandler.connectRequested(SimpleChannelHandler.java:306)
	at org.jboss.netty.channel.SimpleChannelHandler.handleDownstream(SimpleChannelHandler.java:272)
	at org.jboss.netty.channel.DefaultChannelPipeline.sendDownstream(DefaultChannelPipeline.java:591)
	at org.jboss.netty.channel.DefaultChannelPipeline$DefaultChannelHandlerContext.sendDownstream(DefaultChannelPipeline.java:784)
	at org.jboss.netty.handler.codec.oneone.OneToOneEncoder.handleDownstream(OneToOneEncoder.java:54)
	at org.jboss.netty.handler.codec.http.HttpClientCodec.handleDownstream(HttpClientCodec.java:97)
	at org.jboss.netty.channel.DefaultChannelPipeline.sendDownstream(DefaultChannelPipeline.java:591)
	at org.jboss.netty.channel.DefaultChannelPipeline.sendDownstream(DefaultChannelPipeline.java:582)
	at org.jboss.netty.channel.Channels.connect(Channels.java:634)
	at org.jboss.netty.channel.AbstractChannel.connect(AbstractChannel.java:216)
	at com.twitter.finagle.netty3.ChannelConnector.apply(Netty3Transporter.scala:50)
	at com.twitter.finagle.netty3.Netty3Transporter.apply(Netty3Transporter.scala:384)
	at com.twitter.finagle.netty3.Netty3Transporter$$anon$3.apply(Netty3Transporter.scala:193)
	at com.twitter.finagle.client.StdStackClient$$anon$1$$anonfun$1.apply(StackClient.scala:516)
	at com.twitter.finagle.client.StdStackClient$$anon$1$$anonfun$1.apply(StackClient.scala:516)
	at com.twitter.finagle.ServiceFactory$$anon$9.apply(Service.scala:193)
	at com.twitter.finagle.ServiceFactory$$anon$7.apply(Service.scala:150)
	at com.twitter.finagle.Filter$AndThen$$anon$3.apply(Filter.scala:154)
	at com.twitter.finagle.Filter$$anon$2.apply(Filter.scala:104)
	at com.twitter.finagle.service.FailFastFactory.apply(FailFastFactory.scala:208)
	at com.twitter.finagle.pool.CachingPool.apply(CachingPool.scala:58)
	at com.twitter.finagle.pool.WatermarkPool.apply(WatermarkPool.scala:144)
	at com.twitter.finagle.service.ExceptionRemoteInfoFactory.apply(ExceptionRemoteInfoFactory.scala:71)
	at com.twitter.finagle.service.FailureAccrualFactory.apply(FailureAccrualFactory.scala:367)
	at com.twitter.finagle.Filter$$anon$2.apply(Filter.scala:104)
	at com.twitter.finagle.Filter$$anon$2.apply(Filter.scala:104)
	at com.twitter.finagle.Filter$$anon$2.apply(Filter.scala:104)
	at com.twitter.finagle.loadbalancer.LoadBalancerFactory$StackModule$$anon$3.apply(LoadBalancerFactory.scala:175)
	at com.twitter.finagle.ServiceFactoryProxy.apply(Service.scala:208)
	at com.twitter.finagle.ServiceFactoryProxy.apply(Service.scala:208)
	at com.twitter.finagle.loadbalancer.LeastLoaded$Node.apply(LeastLoaded.scala:31)
	at com.twitter.finagle.loadbalancer.Balancer$class.apply(Balancer.scala:193)
	at com.twitter.finagle.loadbalancer.P2CBalancer.apply(P2CBalancer.scala:31)
	at com.twitter.finagle.factory.TrafficDistributor$Distributor.apply(TrafficDistributor.scala:88)
	at com.twitter.finagle.ServiceFactoryProxy.apply(Service.scala:208)
	at com.twitter.finagle.factory.TrafficDistributor.apply(TrafficDistributor.scala:291)
	at com.twitter.finagle.ServiceFactoryProxy.apply(Service.scala:208)
	at com.twitter.finagle.factory.RefcountedFactory.apply(RefcountedFactory.scala:19)
	at com.twitter.finagle.ServiceFactoryProxy.apply(Service.scala:208)
	at com.twitter.finagle.factory.TimeoutFactory.apply(TimeoutFactory.scala:61)
	at com.twitter.finagle.ServiceFactory$$anon$7.apply(Service.scala:150)
	at com.twitter.finagle.ServiceFactory.apply(Service.scala:140)
	at com.twitter.finagle.FactoryToService.apply(Service.scala:283)
	at com.twitter.finagle.ServiceProxy.apply(Service.scala:120)
	at com.twitter.finagle.Service$$anon$1.apply(Service.scala:16)
	at com.twitter.finagle.service.RequeueFilter.com$twitter$finagle$service$RequeueFilter$$applyService(RequeueFilter.scala:70)
	at com.twitter.finagle.service.RequeueFilter.apply(RequeueFilter.scala:107)
	at com.twitter.finagle.Filter$$anon$1.apply(Filter.scala:79)
	at com.twitter.finagle.FactoryToService$$anonfun$apply$4.apply(Service.scala:284)
	at com.twitter.finagle.FactoryToService$$anonfun$apply$4.apply(Service.scala:283)
	at com.twitter.util.Future$$anonfun$flatMap$1.apply(Future.scala:1092)
	at com.twitter.util.Future$$anonfun$flatMap$1.apply(Future.scala:1091)
	at com.twitter.util.Promise$Transformer.liftedTree1$1(Promise.scala:107)
	at com.twitter.util.Promise$Transformer.k(Promise.scala:107)
	at com.twitter.util.Promise$Transformer.apply(Promise.scala:117)
	at com.twitter.util.Promise$Transformer.apply(Promise.scala:98)
	at com.twitter.util.Promise$$anon$1.run(Promise.scala:421)
	at com.twitter.concurrent.LocalScheduler$Activation.run(Scheduler.scala:201)
	at com.twitter.concurrent.LocalScheduler$Activation.submit(Scheduler.scala:159)
	at com.twitter.concurrent.LocalScheduler.submit(Scheduler.scala:239)
	at com.twitter.concurrent.Scheduler$.submit(Scheduler.scala:107)
	at com.twitter.util.Promise.continue(Promise.scala:816)
	at com.twitter.util.Promise$Responder$class.transform(Promise.scala:230)
	at com.twitter.util.Promise.transform(Promise.scala:369)
	at com.twitter.util.Future.flatMap(Future.scala:1091)
	at com.twitter.finagle.FactoryToService.apply(Service.scala:283)
```
