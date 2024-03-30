package com.meinab.smsgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmsGatewayApplication {

    public static void main(String[] args) {
        String defaultServerAddress = "127.0.0.1";
        int defaultPort = 8080;

        String serverAddress = args.length > 0 ? args[0] : defaultServerAddress;
        int port = args.length > 1 ? Integer.parseInt(args[1]) : defaultPort;

        System.setProperty("server.address", serverAddress);
        System.setProperty("server.port", String.valueOf(port));

        SpringApplication.run(SmsGatewayApplication.class, args);
    }

}
