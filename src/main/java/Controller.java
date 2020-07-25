import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;

public class Controller {
    private DataOutputStream dataOutputStream;
    private DataInputStream dateInputStream;
    private Socket clientSocket;
    private UserRepository userRepository;

    public Controller(DataOutputStream dataOutputStream, DataInputStream dataInputStream, Socket clientSocket) throws IOException {
        this.dataOutputStream = dataOutputStream;
        this.dateInputStream = dataInputStream;
        this.clientSocket = clientSocket;
        userRepository = UserRepository.getInstance();
    }

    public void createAccount(String firstName, String lastName, String userName, String password, String repeatedPassword) throws IOException {
        if (!password.equals(repeatedPassword)) {
            sendToClient(ErrorTypes.password_not_matches.getErrorMessage());
        }
        User newUser = new User(userName, password, firstName, lastName, 0);
        try {

            userRepository.save(newUser);
            sendToClient("" + newUser.getAccountNumber());
        }catch (){

        }
    }

    public void getToken(String userName, String password) {


    }

    public void createReceipt(String token, Receipt_type receiptType, long money, int source, int dest, String description) {


    }

    public void getTransaction(String token, String transactionType) {

    }

    public void pay(int id) {

    }

    public void getBalance(String token) {

    }

    public void exit(String token) {

    }

    private User getUserByToken(String token) {
        return null;
    }

    private User getUserByAccountId(int accountId) {
        return null;
    }

    private User getUserByUserName(String userName) {

    }

    public void sendToClient(String message) {
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
