import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BankManager {
    private ServerSocket serverSocket;

    public BankManager() throws IOException {
        serverSocket = new ServerSocket(Constants.port);
    }

    public void start() {
        System.out.println("bank starter with port" + Constants.port);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                new Client(dataOutputStream, dataInputStream, clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
