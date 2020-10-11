package com.alex.chat.server.services;

import com.alex.chat.server.models.Message;
import com.alex.chat.server.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.PrintWriter;

public class UserMessageSender implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(UserMessageSender.class);

    private final PrintWriter writer;

    private final User user;

    public UserMessageSender(PrintWriter writer, User user) {
        this.writer = writer;
        this.user = user;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (user) {
                while (user.isMessageQueueEmpty()) {
                    try {
                        user.wait();
                    } catch (InterruptedException e) {
                        logger.error("Поток прерван", e);
                    }
                }

                Message message = user.pollMessage();
                writer.println(message);
                writer.flush();
            }
        }
    }
}