package client;

import helloworld.GreeterGrpc;
import helloworld.HelloReply;
import helloworld.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


import java.util.concurrent.TimeUnit;

/**
 * @program: baibingGrpcDemo
 * @description:
 * @author: bing.bai
 * @create: 2021-08-17 10:21
 */

public class HelloWorldClient {

  private final ManagedChannel channel;
  private final GreeterGrpc.GreeterBlockingStub blockingStub;

  public HelloWorldClient(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();

    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void greet(String name) {
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response;
    try {
      response = blockingStub.sayHello(request);
    } catch (StatusRuntimeException e) {
      System.out.println("RPC failed: {0}");
      return;
    }
    System.out.println(("Message from gRPC-Server: " + response.getMessage()));
  }

  public static void main(String[] args) throws InterruptedException {
    HelloWorldClient client = new HelloWorldClient("127.0.0.1", 50051);
    try {
      String user = "world";
      client.greet(user);
    } finally {
      client.shutdown();
    }
  }
}
