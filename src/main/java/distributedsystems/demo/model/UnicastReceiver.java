package distributedsystems.demo.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class UnicastReceiver extends Thread{
    String message;
    int port;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            socket = serverSocket.accept();
            message = dataInputStream.readUTF();
            System.out.println(message);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public int getPort() {
        return port;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
