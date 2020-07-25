import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client extends Thread {
    private DataInputStream dateInputStream;
    private Controller controller;

    public Client(DataOutputStream dataOutputStream, DataInputStream dataInputStream, Socket clientSocket) {
        this.dateInputStream = dataInputStream;
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
            Matcher matcher = Pattern.compile(Constants.CREATE_ACCOUNT).matcher(command);
            matcher.find();
            controller.createAccount(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
        } else if (command.matches(Constants.GET_TOKEN)) {
            Matcher matcher = Pattern.compile(Constants.GET_TOKEN).matcher(command);
            matcher.find();
            controller.getToken(matcher.group(1), matcher.group(2));
        } else if (command.matches(Constants.CREATE_RECEIPT)) {
            Matcher matcher = Pattern.compile(Constants.CREATE_RECEIPT).matcher(command);
            matcher.find();
            controller.createReceipt(matcher.group(1), Receipt_type.getType(matcher.group(2)), Long.parseLong(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), matcher.group(6));
        } else if (command.matches(Constants.GET_TRANSACTION)) {
            Matcher matcher = Pattern.compile(Constants.GET_TRANSACTION).matcher(command);
            matcher.find();
            controller.getTransaction(matcher.group(1), matcher.group(2));
        } else if (command.matches(Constants.GET_BALANCE)) {
            Matcher matcher = Pattern.compile(Constants.GET_BALANCE).matcher(command);
            matcher.find();
            controller.getBalance(matcher.group(1));
        } else if (command.matches(Constants.PAY)) {
            Matcher matcher = Pattern.compile(Constants.PAY).matcher(command);
            matcher.find();
            controller.pay(Integer.parseInt(matcher.group(1)));
        } else if (command.matches(Constants.EXIT)) {
            Matcher matcher = Pattern.compile(Constants.EXIT).matcher(command);
            matcher.find();
            controller.exit(matcher.group(1));
        } else {
            controller.sendToClient("invalid input");
        }
    }
}
