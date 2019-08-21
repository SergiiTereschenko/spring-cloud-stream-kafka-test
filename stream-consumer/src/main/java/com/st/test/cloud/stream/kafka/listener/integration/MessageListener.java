package com.st.test.cloud.stream.kafka.listener.integration;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@EnableBinding({MessageStream.class, Source.class})
public class MessageListener {

    ObjectMapper objectMapper = new ObjectMapper();

    @StreamListener
    @Output(Processor.OUTPUT)
    public Flux<Message> onNextMessage(@Input(MessageStream.MESSAGE_IN) Flux<String> messageFlux) {
        return messageFlux.doOnNext(message -> log.info("Got new message {}", message))
            .flatMap(str -> deserialize(str, Message.class));
//            .doOnError(e -> {
//                log.error("Error during processing: {}", e.getMessage());
//            })
//            .onErrorResume(e -> Flux.just(new Message(0, "Default Value")))
//            .retry();
    }

//    @StreamListener
//    public void onNextMessage(@Input(MessageStream.MESSAGE_IN) Flux<Message> messageFlux) {
//        messageFlux
//            //.flatMap(e -> Mono.error(new RuntimeException("444")))
//            .doOnError(e -> log.error("Error during processing: ", e))
//            .retry()
//            .subscribe(
//                message -> log.info("Got new message {}", message),
//                e -> log.error("Error consumer", e),
//                () -> log.warn("Complete consumer")
//            );
//    }

    @SneakyThrows
    public <T> Mono<T> deserialize(String str, Class<T> clazz) {
        return Mono.just(str).map(s -> {
            try {
                return objectMapper.readValue(s, clazz);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        })
            .doOnError(e -> {
            log.error("Error during processing: {}", e.getMessage());
        })
            .onErrorResume(thr -> Mono.just((T) new Message(0, "Default Value")));
    }
}
