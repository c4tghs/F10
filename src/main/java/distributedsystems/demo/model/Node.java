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

/**
 * @author Thomas Somers
 * @version 1.0 26/03/2020 9:54
 */

@Data
public class Node {
    private String name;
    private String ip;

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
                .url("http://localhost:8080/findFile/" + fileName)
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
        try {
            messagePublisher.multicast(name,multicastAddress);
        } catch (IOException e) {
            System.out.println("Bootstrap initial multicast failed!");
        }

        //Receive message from namingServer
        multicastReceiver.setMultiCastAddress(ip);
        multicastReceiver.setPort(4446);

        multicastReceiver.run();

        String message = multicastReceiver.getMessage();


        if (Integer.parseInt(message) == 1) {
            this.prevHash = generateHash(this.name);
            this.nextHash = generateHash(this.name);

        } else{

        }

    }

    private int getIpFromHash(int hash) {
        Request requestNext = new Request.Builder()
                .url("localhost:8080/ip/" + hash)
                .build();

        try {
            Response response = okHttpClient.newCall(requestNext).execute();

            return Integer.parseInt(response.body().string());
        } catch (IOException e) {
            System.out.println("No response from server: Get ip from hash!");
        }

        return 0;
    }

    public void listenToMulticast(String multicastAddress, int port) {
        multicastReceiver.setMultiCastAddress(multicastAddress);
        multicastReceiver.setPort(port);

        multicastReceiver.run();

        String message = multicastReceiver.getMessage();
        InetAddress senderAddress = multicastReceiver.getSenderAddress();

        int senderHash = generateHash(message);
        int myHash = generateHash(name);

        if (myHash < senderHash && senderHash < nextHash) {
            nextHash = senderHash;
            try {
                //TODO: message bekijken
                messagePublisher.unicast(String.valueOf(senderHash), String.valueOf(senderAddress), 1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (prevHash < senderHash && senderHash < myHash) {
            prevHash = senderHash;
            try {
                //TODO: message bekijken
                messagePublisher.unicast(String.valueOf(senderHash), String.valueOf(senderAddress), 1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }









    //?????????

    public void discover(String address) throws IOException {
        String message = String.format("Hey, I am %s with ip: %s", name, ip);
        messagePublisher.multicast(message, address);
    }

}
