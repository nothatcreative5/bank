import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;

public class Controller {
    private DataOutputStream dataOutputStream;
    private DataInputStream dateInputStream;
    private Socket clientSocket;
    public static int port = 8090;
    public static final String CREATE_ACCOUNT = "^create_account (\\S+) (\\S+) (\\S+) (\\S+) (\\S+)$";
    public static final String GET_TOKEN = "^get_token (\\S+) (\\S+)$";
    public static final String CREATE_RECEIPT = "^create_receipt (\\S+) (\\S+) (\\d+) (-?\\d+) (-?\\d+) (.*)";
    public static final String GET_TRANSACTION = "^get_transactions (\\S+) (\\S+)$";
    public static final String PAY = "^pay (\\d+)";
    public static final String GET_BALANCE = "^get_balance (.*)";
    public static final String EXIT = "^exit$";
    public Controller(DataOutputStream dataOutputStream, DataInputStream dataInputStream, Socket clientSocket) {
        this.dataOutputStream = dataOutputStream;
        this.dateInputStream = dataInputStream;
        this.clientSocket = clientSocket;
    }

    public void createAccount(String token,String userName,String password,String firstName,String lastName){


    }
    public void getToken(String userName,String password){

    }
    public void createReceipt(String token,Receipt_type receiptType,long money,int source,int dest,String description){

    }
    public void getTransaction(String token ,Receipt_type transactionType){

    }
    public void sendToClient(String message){
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
