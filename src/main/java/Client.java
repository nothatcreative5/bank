import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client extends Thread {
    private DataOutputStream dataOutputStream;
    private DataInputStream dateInputStream;
    private Socket clientSocket;
    private Controller controller;

    public Client(DataOutputStream dataOutputStream, DataInputStream dataInputStream, Socket clientSocket) {
        this.dataOutputStream = dataOutputStream;
        this.dateInputStream = dataInputStream;
        this.clientSocket = clientSocket;
        controller = new Controller(dataOutputStream, dataInputStream, clientSocket);
    }

    @Override
    public void run() {
        try {
            String command = dateInputStream.readUTF();
            commandProcess(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void commandProcess(String command) {
        if (command.matches(Constants.CREATE_ACCOUNT)) {

        } else if (command.matches(Constants.GET_TOKEN)) {

        } else if (command.matches(Constants.CREATE_RECEIPT)) {

        } else if (command.matches(Constants.GET_TRANSACTION)) {

        } else if (command.matches(Constants.GET_BALANCE)) {

        } else if (command.matches(Constants.PAY)) {

        } else if (command.matches(Constants.EXIT)) {

        } else {
            controller.sendToClient("invalid input");
        }
    }

}
