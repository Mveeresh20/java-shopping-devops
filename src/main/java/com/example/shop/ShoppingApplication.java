package com.example.shop;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ShoppingApplication {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        // Home endpoint
        server.createContext("/", exchange -> {

            String response = "Welcome to Java Shopping Application!";

            exchange.sendResponseHeaders(
                    200,
                    response.getBytes().length
            );

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.close();
        });

        // Health endpoint
        server.createContext("/health", exchange -> {

            String response = "UP";

            exchange.sendResponseHeaders(
                    200,
                    response.getBytes().length
            );

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.close();
        });

        server.start();

        System.out.println(
                "Java Shopping Application started on port 8080"
        );
    }
}
