package client;

import java.io.*;
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
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream.writeUTF(command);
            dataOutputStream.flush();
            System.out.println(dataInputStream.readUTF());
            socket.close();
        }
    }
}
