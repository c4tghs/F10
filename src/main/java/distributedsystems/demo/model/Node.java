package distributedsystems.demo.model;

import com.squareup.okhttp.*;
import lombok.Data;

import java.io.File;
import java.io.IOException;

import java.net.InetAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:54
 */

@Data
public class Node {
    private String name;
    private String ip;

    private String serverAddress = "10.0.22.3";
    private int nextHash;
    private int prevHash;

    private static String multicastAddress = "12.12.12.12";//TODO: multicast instellen

    private Map<String, List<File>> files;

    private OkHttpClient okHttpClient;
    private MulticastReceiver multicastReceiver;
    private MessagePublisher messagePublisher;


    public Node(String name, String ip) {
        this.name = name;
        this.ip = ip;
        files = new TreeMap<>(); //TODO : soort map nakijken + add/remove file
        okHttpClient = new OkHttpClient();
        multicastReceiver = new MulticastReceiver();
        messagePublisher = new MessagePublisher();

        bootstrapMulticast();
    }

    public String findFile(String fileName) {
        String ip = "";

        Request request = new Request.Builder()
                .url(serverAddress + "/findFile/" + fileName)
                .get()
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            ip = response.body().string();
        } catch (IOException e) {
            System.out.println("Findfile niet gelukt!");
        }

        return ip;
    }

    private int generateHash(String name) {
        return name.hashCode() % 32768;
    }

    private void bootstrapMulticast() {
        String message = "Name : " + name + "ip : " + ip;
        try {
            messagePublisher.multicast(message,multicastAddress);
        } catch (IOException e) {
            System.out.println("Bootstrap initial multicast failed!");
        }
    }

    public void receiveMessage(String multicastAddress, int port) {
        multicastReceiver.start();
        multicastReceiver.setMessage(multicastAddress);
        multicastReceiver.setPort(port);
        multicastReceiver.run();

        multicastReceiver.run();

        String message = multicastReceiver.getMessage();
        InetAddress senderAddress = multicastReceiver.getSenderAddress();

        int senderHash = generateHash(message);
        int myHash = generateHash(name);

        if (myHash < senderHash && senderHash < nextHash) {
            nextHash = senderHash;
            try {
                //TODO: message bekijken
                messagePublisher.unicast(String.valueOf(senderHash), String.valueOf(senderAddress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (prevHash < senderHash && senderHash < myHash) {
            prevHash = senderHash;
            try {
                //TODO: message bekijken
                messagePublisher.unicast(String.valueOf(senderHash), String.valueOf(senderAddress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void discover(String address) throws IOException {
        String message = String.format("Hey, I am %s with ip: %s", name, ip);
        messagePublisher.multicast(message, address);
        // start replication
        startReplication();
    }
    public void startReplication() {
        for (Map.Entry<String,List<File>> entry : files.entrySet()) {
            int hash = generateHash(entry.getKey());
            String json = "{\"hash\":" + hash + "}";
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
            // IP namingserver:
            Request request = new Request.Builder()
                    .url(serverAddress + "/file/hash" + hash)
                    .post(body)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
