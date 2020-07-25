package bank;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private static ServerSocket serverSocket;

    public static void main(String[] args){
        if (args.length > 0) {
            try {
                int port = Integer.parseInt(args[0]);
                Constants.port = port;
            } catch (NumberFormatException e) {
            }
        }
        try {
            BankManager bankManager = new BankManager();
            bankManager.start();
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("failed");
        }
    }
}
