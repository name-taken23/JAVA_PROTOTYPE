package uk.co.ecsitsolutions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import uk.co.ecsitsolutions.oneM2Mrepo.OneM2MWrapper;
import uk.co.ecsitsolutions.oneM2Mrepo.OneM2MmqttWrapper;
import uk.co.ecsitsolutions.oneM2Mrepo.models.Ae;

import java.util.ArrayList;
import java.util.Map;

public class IPE {

    static String cavTopic = "CAV/latlng";
    static String cseRequestTopic = "/oneM2M/req/CIPE/id-in/json";
    static String serverURI = "tcp://127.0.0.1:1883";
    static String clientID = "IPE";

    static MqttClient ipeClient;
    static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws MqttException {
        ipeClient = new MqttClient(serverURI, clientID);
        ipeClient.connect();
        ipeClient.subscribe(cavTopic, 1);
        ObjectMapper mapper1 = new ObjectMapper();
        if(ipeClient.isConnected()){
            Map<String, Object> oneM2MWrapperObject;
            OneM2MmqttWrapper mqttWrapper = new OneM2MmqttWrapper();

            ArrayList<String> srv = new ArrayList<>();
            srv.add("3");
            Ae ae = new Ae();
            ae.setRn("CAV");
            ae.setApi("NCAV");
            ae.setRr(true);
            ae.setSrv(srv);
            oneM2MWrapperObject = OneM2MWrapper.createOneM2MWrapper("m2m:ae", ae);

            mqttWrapper.op = 1;
            mqttWrapper.to = "/id-in/cse-in";
            mqttWrapper.fr = "CIPE";
            mqttWrapper.rqi = "1234";
            mqttWrapper.rvi = "3";
            mqttWrapper.pc = oneM2MWrapperObject;
            mqttWrapper.ty = 2;

            try {
                String content = mapper1.writeValueAsString(mqttWrapper);
                System.out.println(content);
                MqttMessage payload = new MqttMessage(content.getBytes());
                ipeClient.publish(cseRequestTopic, payload);
            } catch (JsonProcessingException | MqttException e) {
                throw new RuntimeException(e);
            }

        }
        ipeClient.setCallback(new MqttCallback() {
            @Override
            public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {

            }

            @Override
            public void mqttErrorOccurred(MqttException e) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("topic: " + s);
                System.out.println("qos: " + mqttMessage.getQos());
                System.out.println("content: " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttToken iMqttToken) {

            }

            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void authPacketArrived(int i, MqttProperties mqttProperties) {

            }
        });
    }


}

