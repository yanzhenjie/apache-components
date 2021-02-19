package org.apache.httpcore.sample;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.RequestLine;
import org.apache.httpcore.config.SocketConfig;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.impl.bootstrap.HttpServer;
import org.apache.httpcore.impl.bootstrap.ServerBootstrap;
import org.apache.httpcore.protocol.HttpContext;
import org.apache.httpcore.protocol.HttpRequestHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestHost {

    public static void main(String[] args) throws Exception {
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .setSoReuseAddress(true)
                .setTcpNoDelay(true)
                .build();

        final HttpServer server = ServerBootstrap.bootstrap()
                .setListenerPort(8080)
                .setServerInfo("TestServer/1.1")
                .setSocketConfig(socketConfig)
                .registerHandler("/testHost", new TestHostApi())
                .create();

        server.start();

        System.out.println("------ Server is started.------");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.shutdown(5, TimeUnit.SECONDS);
            }
        });
    }

    static class TestHostApi implements HttpRequestHandler {


        public TestHostApi() {
        }

        @Override
        public void handle(HttpRequest request, HttpResponse response,
                           HttpContext context) throws HttpException, IOException {
            RequestLine requestLine = request.getRequestLine();
            String uri = requestLine.getUri();

            System.out.println("---------------------------");
            System.out.println("----- uri");
            System.out.println(uri);
            System.out.println("----- local info");
            System.out.println("getLocalAddr：" + request.getLocalAddr());
            System.out.println("getLocalName：" + request.getLocalName());
            System.out.println("getLocalPort：" + request.getLocalPort());
            System.out.println("----- remote info");
            System.out.println("getRemoteAddr：" + request.getRemoteAddr());
            System.out.println("getRemoteHost：" + request.getRemoteHost());
            System.out.println("getRemotePort：" + request.getRemotePort());

            String data = "Hello World.";
            response.setEntity(new StringEntity(data));
        }

    }

}
