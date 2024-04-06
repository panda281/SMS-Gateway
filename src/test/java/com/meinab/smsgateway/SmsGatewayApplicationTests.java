package com.meinab.smsgateway;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SmsGatewayApplicationTests {

    @BeforeEach
    public void setUp() {
        System.clearProperty("server.address");
        System.clearProperty("server.port");
    }

    @AfterEach
    public void tearDown() {
        System.clearProperty("server.address");
        System.clearProperty("server.port");
    }

    @Test
    @DisplayName("Given no argument, when the argument is empty, then it sets default port and Server address")
    void givenNoArgument_WhenTheArgumentIsEmpty_ThenItSetsDefaultPortAndServerAddress() {
        String[] args = {};
        SmsGatewayApplication.configureApplication(args);

        assertEquals("127.0.0.1", System.getProperty("server.address"));
        assertEquals("8080", System.getProperty("server.port"));
    }

    @Test
    @DisplayName("Given a server address and port argument, when the server address and port correct, then it overrides the default port and server address")
    void testConfigureApplicationWithArguments() {
        String[] args = {"192.168.1.100", "9090"};
        SmsGatewayApplication.configureApplication(args);

        assertEquals("192.168.1.100", System.getProperty("server.address"));
        assertEquals("9090", System.getProperty("server.port"));
    }

    @Test
    @DisplayName("Given a server address and port argument, when the server address or/and port is not correct, then it throws NumberFormatException")
    void testConfigureApplicationWithInvalidPortArgument() {
        String[] args = {"192.168.1.100", "invalidPort"};

        Assertions.assertThrows(NumberFormatException.class, () -> {
            SmsGatewayApplication.configureApplication(args);
        });
    }

    @Test
    @DisplayName("Given a server address and port argument, when the server address and port is correct, then it starts the server")
    void  testMainMethod(){
        String[] args = {"127.0.0.1", "10054"};
        SmsGatewayApplication.main(args);

        assertEquals("127.0.0.1", System.getProperty("server.address"));
        assertEquals("10054", System.getProperty("server.port"));
    }

}
