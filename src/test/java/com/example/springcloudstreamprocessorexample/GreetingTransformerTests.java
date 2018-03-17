package com.example.springcloudstreamprocessorexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GreetingTransformerTests {

    @Autowired
    private Processor processor;

    @Autowired
    private MessageCollector collector;

    @Test
    public void testTransformer() {
        SubscribableChannel input = this.processor.input();

        String msg = "<greeting><from>Zoltan</from><value>Grüezi</value></greeting>";

        input.send(new GenericMessage<>(msg.getBytes(StandardCharsets.UTF_8)));

        BlockingQueue<Message<?>> messages = this.collector.forChannel(processor.output());

        assertThat(messages, receivesPayloadThat(is("{\"message\":\"Hallo Zoltan\"}")));
    }
}
