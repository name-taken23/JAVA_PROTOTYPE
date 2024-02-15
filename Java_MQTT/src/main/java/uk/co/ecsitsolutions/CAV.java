package uk.co.ecsitsolutions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.util.Random;

public class CAV {

    static String topic = "CAV/latlng";
    static String serverURI = "tcp://127.0.0.1:1883";
    static String clientID = "CAV";
    public static void main(String[] args) throws MqttException, JsonProcessingException, InterruptedException {
        Random rand = new Random();
        CAVpayload cav = new CAVpayload();
        MqttClient client = new MqttClient(serverURI, clientID);
        client.connect();
        ObjectMapper mapper = new ObjectMapper();
        while(true){
            double randomLat = rand.nextDouble(53.3409, 53.8409);
            double randomLng = rand.nextDouble(-6.2625, -6.0625);
            cav.setPayload(randomLat, randomLng);
            String content = mapper.writeValueAsString(cav);
            System.out.println("Payload: " + content);

            MqttMessage payload = new MqttMessage(content.getBytes());
            client.publish(topic, payload);
            Thread.sleep(5000);
        }
    }


    public static class CAVpayload {

        public double latitude;
        public double longitude;

        void setPayload(double lat, double lng){
            this.latitude = lat;
            this.longitude = lng;
        }

    }
}
