package com.example.springcloudstreamprocessorexample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.MimeType;

import java.io.IOException;

@Configuration
public class GreetingTransformer {

    private static final Logger logger = LoggerFactory.getLogger(GreetingTransformer.class);

    @ServiceActivator(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    //@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public GreetingResponse transform(Greeting greeting) {
        logger.info("received greeting message" + greeting);
        GreetingResponse response = new GreetingResponse("Hallo " + greeting.getFrom());
        return response;
    }

    @Bean
    @StreamMessageConverter
    public MessageConverter greetingConverter() {
        return new GreetingMessageConverter();
    }


    static class GreetingMessageConverter extends AbstractMessageConverter {

        private final ObjectMapper objectMapper;

        public GreetingMessageConverter() {
            super(MimeType.valueOf("application/xml"));
            objectMapper = new XmlMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return clazz.equals(Greeting.class);
        }

        @Override
        protected Object convertFromInternal(Message<?> message, Class<?> targetClass, @Nullable Object conversionHint) {
            try {
                return objectMapper.readValue((byte[]) message.getPayload(), Greeting.class);
            } catch (IOException e) {
                return null;
            }
        }
    }

}
