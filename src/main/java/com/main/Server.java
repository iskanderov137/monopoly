package com.main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;

public class Server {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(1234), 0);
        server.createContext("/rollDice", new RollDiceHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on port 1234");
    }

    static class RollDiceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Random random = new Random();
            int result1 = random.nextInt(6) + 1;
            int result2 = random.nextInt(6) + 1;

            String response = "{" +
                    "\"dice1\": " + result1 + "," +
                    "\"dice2\": " + result2 +
                    "}";

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
