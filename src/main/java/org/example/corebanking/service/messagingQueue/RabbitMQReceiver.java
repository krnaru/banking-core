package org.example.corebanking.service.messagingQueue;

import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    /*
        @RabbitListener(queues = "accountQueue")
    */
    public void receiveMessage(String message) {
        System.out.println("Received Message: " + message);
    }

}