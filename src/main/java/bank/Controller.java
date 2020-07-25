package bank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import bank.exception.InvalidTokenException;
import bank.exception.TokenExpiredException;

public class Controller {
    private DataOutputStream dataOutputStream;
    private DataInputStream dateInputStream;
    private Socket clientSocket;
    private Object lock;
    private UserRepository userRepository;
    private ReceiptRepository receiptRepository;

    public Controller(DataOutputStream dataOutputStream, DataInputStream dataInputStream, Socket clientSocket, Object lock) throws IOException {
        this.dataOutputStream = dataOutputStream;
        this.dateInputStream = dataInputStream;
        this.clientSocket = clientSocket;
        this.lock = lock;
        userRepository = UserRepository.getInstance();
        receiptRepository = ReceiptRepository.getInstance();
    }

    public void createAccount(String firstName, String lastName, String userName, String password, String repeatedPassword) throws IOException {
        synchronized (lock) {
            User user = getUserByUserName(userName);
            if (user != null) {
                sendToClient(ErrorTypes.username_is_taken.getErrorMessage());
            }
            if (!password.equals(repeatedPassword)) {
                sendToClient(ErrorTypes.password_not_matches.getErrorMessage());
            }
            User newUser = new User(userName, password, firstName, lastName, 0);
            userRepository.save(newUser);
            sendToClient("" + newUser.getAccountNumber());
        }
    }

    public void getToken(String userName, String password) {
        User user = getUserByUserName(userName);
        if (user == null) {
            sendToClient(ErrorTypes.invalid_password_username.getErrorMessage());
        }
        if (!user.getPassword().equals(password)) {
            sendToClient(ErrorTypes.invalid_password_username.getErrorMessage());
        }
        sendToClient(Session.getToken(userName));
    }

    public void createReceipt(String token, Receipt_type receiptType, long money, int source, int dest, String description) throws IOException {
        try {
            isTokenValid(token);
            if (receiptType == null) {
                sendToClient("invalid receipt type”");
                return;
            }
            if (money <= 0) {
                sendToClient("invalid money");
                return;
            }
            User sourceAccount = getUserByAccountId(source);
            if (sourceAccount == null) {
                sendToClient("source account id is invalid");
                return;
            }
            User destAccount = getUserByAccountId(dest);
            if (sourceAccount == null) {
                sendToClient("dest account id is invalid");
                return;
            }
            if (destAccount.equals(sourceAccount)) {
                sendToClient("equal source and dest account");
                return;
            }
            if (source == -1 || dest == -1) {
                sendToClient("invalid account id");
                return;
            }

            Receipt receipt = new Receipt(receiptType, money, "" + source, "" + dest, description);
            receiptRepository.save(receipt);
            //TODO send succes
            // sendToClient();
        } catch (TokenExpiredException e) {
            sendToClient("token expired");
        } catch (InvalidTokenException e) {
            sendToClient("token is invalid");
        }
    }

    public void getTransaction(String token, String transactionType) {
        isTokenValid(token);
    }

    public void pay(int id) {

    }

    public void getBalance(String token) {
        try {
            isTokenValid(token);
            User user = getUserByToken(token);
            if (user == null) {
                sendToClient(ErrorTypes.token_is_invalid.getErrorMessage());
                return;
            }
            sendToClient("" + user.getCredit());
        } catch (TokenExpiredException e) {
            e.printStackTrace();
        } catch (InvalidTokenException e) {
            e.printStackTrace();
        }

    }

    public void exit(String token) {

    }

    private User getUserByToken(String token) {
        String username = Session.getUsernameByToken(token);
        if(username == null)
            return null;
        else
            return userRepository.getUserByUsername(username);
    }

    private User getUserByAccountId(int accountId) {
        return userRepository.getUserByAccountId(accountId);
    }

    private User getUserByUserName(String userName) {
        return userRepository.getUserByUsername(userName);
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

    private boolean isTokenValid(String token) throws TokenExpiredException, InvalidTokenException {
        Session.tokenStatus tokenStatus = Session.isTokenExpired(token);
        if (tokenStatus == Session.tokenStatus.Expired)
            throw new TokenExpiredException();
        if (tokenStatus == Session.tokenStatus.Invalid)
            throw new InvalidTokenException();
        return true;
    }
}
