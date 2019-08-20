package com.st.test.kafka.consumer.model;

import lombok.Data;

@Data
public class Message {
    long id;
    String content;
}
