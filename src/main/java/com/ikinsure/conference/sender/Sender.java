package com.ikinsure.conference.sender;

public interface Sender {
    boolean send(String destination, String text);
}
