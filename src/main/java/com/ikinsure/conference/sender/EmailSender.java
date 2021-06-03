package com.ikinsure.conference.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class EmailSender implements Sender {

    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    @Override
    public boolean send(String destination, String text) {
        try {
            Files.writeString(Paths.get(destination), text, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("An exception occurred!", e);
            return false;
        }
        return true;
    }
}
