package client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static int bankPort = 8090;
    private static Socket socket;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            command = scanner.nextLine();
            socket = new Socket("localhost", bankPort);
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dataOutputStream.writeUTF(command);
            dataOutputStream.flush();
            socket.close();
        }
    }
}
