package com.example.springcloudstreamprocessorexample;

public class Greeting {

    private String from;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
