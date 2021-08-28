package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBus mbImpl;
    private ExampleMicroService exampleMicroService ;
    private ExampleEvent eventForCheck;
    private ExampleBroadcast broadcastForCheck;
    private Message messageFetched;

    @BeforeEach
    void setUp() {
        mbImpl = MessageBusImpl.getInstance(); // MessageBusImpl is a singleton
        exampleMicroService = new ExampleMicroService();
        eventForCheck = new ExampleEvent();
        broadcastForCheck = new ExampleBroadcast();
    }

    @AfterEach
    void tearDown() {
        mbImpl.unregister(exampleMicroService); // after each test we unregister the microService & that way we check 'unregister()' functionality
        messageFetched = null; //after each test we want to initialize 'messageFetched' with null in order to test the other methods
    }

    @Test
    void subscribeEvent() {  // NOTICE - we also check here 'sendEvent()'
        // by registering to 'mbImpl' we also check 'register()' functionality
        mbImpl.register(exampleMicroService); // create a queue for the Microservice - 'exampleMicroService' in the Message-Bus
        mbImpl.subscribeEvent(eventForCheck.getClass(), exampleMicroService);
        Future<Integer> eSend = mbImpl.sendEvent(eventForCheck);
        assertNotNull(eSend);  // check the event was added to the messages queue of 'hanSoloMS'
        try {
            // NOTICE - this is also test for the 'awaitMessage()' method
            messageFetched = mbImpl.awaitMessage(exampleMicroService); // 'exampleMicroService' takes the message from it's queue
            assertNotNull(messageFetched); // check 'messageFetched' - the message 'exampleMicroService' fetch from the queue not null
        }
        catch (Exception e){ // if we arrive to this line - means that there was no message in the queue - therefore - error
            fail();
        }
    }

    @Test
    void subscribeBroadcast() {
        // by registering to 'mbImpl' we also check 'register()' functionality
        mbImpl.register(exampleMicroService);// create a queue for the Microservice - 'exampleMicroService' in the Message-Bus
        mbImpl.subscribeBroadcast(broadcastForCheck.getClass(), exampleMicroService); //'exampleMicroService' subscribes itself 'ExampleBroadcast' messages
        mbImpl.sendBroadcast(broadcastForCheck); // send broadcast message to those who register to get those kind of messages
        try {
            // NOTICE - this is also test for the 'awaitMessage()' method
            messageFetched = mbImpl.awaitMessage(exampleMicroService);
            assertNotNull(messageFetched); // check 'messageFetched' - the message 'exampleMicroService' fetch from the queue not null
        }
        catch (Exception e){ // if we arrive to this line - means that there was no broadcast message in the queue - therefore - error
            fail();
        }
    }

    @Test
    void complete() { // NOTICE - 'sendEvent()' is checked here as well
        mbImpl.register(exampleMicroService); // create a queue for the Microservice - 'msHandleExampleEvent' in the Message-Bus
        mbImpl.subscribeEvent(eventForCheck.getClass(), exampleMicroService); // events from type 'ExampleEvent' can be handled by msHandleExampleEvent
        Future<Integer> futureReceiveByMSHandle = mbImpl.sendEvent(eventForCheck); // 'exampleMicroService' send event from type 'ExampleEvent' to MessageBus
        try {
            messageFetched = mbImpl.awaitMessage(exampleMicroService); // 'msHandleExampleEvent' fetch the message from the queue
            assertNotNull(messageFetched); // check 'messageFetched' - the message 'msHandleExampleEvent' fetch from the queue not null
        }
        catch (Exception e){ // if we arrive to this line - means that there was no message in the queue - therefore - error
            fail();
        }
        mbImpl.complete(eventForCheck,2); // 'msHandleExampleEvent' notify that event was handled.
        assertNotNull(futureReceiveByMSHandle.get()); // before check the given result, first check is to validate it's not null
        assertTrue(2 == futureReceiveByMSHandle.get()); // check if the Future object associated with 'eventForCheck' is resolved to the result given as a parameter.
    }

    @Test
    void sendBroadcast() { // Notice - same test as 'subscribeBroadcast()'
        mbImpl.register(exampleMicroService); // create a queue for the Microservice - 'exampleMicroService' in the Message-Bus
        mbImpl.subscribeBroadcast(broadcastForCheck.getClass(), exampleMicroService); // events from type 'ExampleBroadcast' can be handled by 'exampleMicroService'
        mbImpl.sendBroadcast(broadcastForCheck); // this should send the event to 'exampleMicroService' queue
        try {
            messageFetched = mbImpl.awaitMessage(exampleMicroService);
            assertNotNull(messageFetched); // check 'messageFetched' - the message 'exampleMicroService' fetch from the queue not null
        }
        catch (Exception e){ // if we arrive to this line - means that there was no broadcast message in the queue - therefore - error
            fail();
        }
    }
}