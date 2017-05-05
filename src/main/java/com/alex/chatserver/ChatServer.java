package com.alex.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static MessagesSender messagesSender = new MessagesSender();
    private static ExecutorService threads = Executors.newCachedThreadPool();

    public static void run() throws SQLException, IOException, InterruptedException {

        ServerSocket server = new ServerSocket(5003);

        System.out.println("Server is up!");

        while (true) {

            // Waiting for new user
            Socket socket = server.accept();

            // Creating new Thread for user-server connection and run it
            User user = new User(socket, messagesSender);
            threads.execute(user);

            // Information about connection
            String userName = user.getUserName();
            String message = String.format("%s joined the chat", userName);
            System.out.println(message);

            // Add clients to messagesSender's collection
            messagesSender.addUser(user);
        }
    }
}
