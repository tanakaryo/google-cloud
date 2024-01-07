package pubsuttest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

public class SubscriberAsyncMain {
    
    public static void main(String[] args) throws Exception {
        String projectId = "dataflowtest002";
        String subscriptionId = "test-sub";

        subscribeAsyncMain(projectId, subscriptionId);
    }

    public static void subscribeAsyncMain(String projectId, String subscriptionId) {

        ProjectSubscriptionName projectSubscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        MessageReceiver messageReceiver = 
        (PubsubMessage message, AckReplyConsumer consumer ) -> {
            System.out.println("Id:" + message.getMessageId());
            System.out.println("Data:" + message.getData().toStringUtf8());
            consumer.ack();
        };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(projectSubscriptionName, messageReceiver).build();
            subscriber.startAsync().awaitRunning();
            System.out.printf("Listening for messages on %s:\n", projectSubscriptionName.toString());
            subscriber.awaitTerminated(30, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            subscriber.stopAsync();
        }
    }


}
