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
    private Object payingLock;
    private UserRepository userRepository;
    private ReceiptRepository receiptRepository;

    public Controller(DataOutputStream dataOutputStream, DataInputStream dataInputStream, Socket clientSocket, Object lock, Object payingLock) throws IOException {
        this.dataOutputStream = dataOutputStream;
        this.dateInputStream = dataInputStream;
        this.clientSocket = clientSocket;
        this.payingLock = payingLock;
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
            if (destAccount == null) {
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

            Receipt receipt = new Receipt(receiptType, money, source, dest, description);
            receiptRepository.save(receipt);
            sendToClient("" + receipt.getReceiptId());
        } catch (TokenExpiredException e) {
            sendToClient(ErrorTypes.token_expired.getErrorMessage());
        } catch (InvalidTokenException e) {
            sendToClient(ErrorTypes.token_is_invalid.getErrorMessage());
        }
    }

    public void getTransaction(String token, String transactionType) {
        try {
            String message = null;
            isTokenValid(token);
            User user = getUserByToken(token);
            if (user == null) {
                sendToClient(ErrorTypes.token_is_invalid.getErrorMessage());
                return;
            }
            try {
                Integer.parseInt(transactionType);
                message = receiptRepository.getTransaction(user, transactionType);
            } catch (NumberFormatException e) {
                if (!transactionType.equals("+") && !transactionType.equals("-") && !transactionType.equals("*")) {
                    sendToClient("your input contains invalid characters");
                    return;
                }
                message = receiptRepository.getTransaction(user, transactionType);
            }
            if (message == null) {
                sendToClient("{}");
                return;
            }
            sendToClient(message);
        } catch (TokenExpiredException e) {
            sendToClient(ErrorTypes.token_expired.getErrorMessage());
        } catch (InvalidTokenException e) {
            sendToClient(ErrorTypes.token_is_invalid.getErrorMessage());
        }
    }

    public void pay(int id) throws IOException {
        synchronized (payingLock) {
            Receipt receipt = receiptRepository.getReceiptById(id);
            if (receipt == null) {
                sendToClient("invalid receipt id");
                return;
            }
            if (receipt.getPaid()) {
                sendToClient("receipt is paid before");
                return;
            }
            switch (receipt.getReceipt_type()) {
                case withdraw:
                    withDraw(receipt);
                    return;
                case move:
                    move(receipt);
                    return;
                case deposit:
                    deposit(receipt);
            }
        }
    }

    public void withDraw(Receipt receipt) throws IOException {
        User destUser = getUserByAccountId(receipt.getDestId());
        long money = receipt.getMoney();
        if (destUser == null) {
            sendToClient("invalid account id");
            return;
        }
        if (destUser.getCredit() <= money) {
            sendToClient("dest account does not have enough money");
            return;
        }
        destUser.setCredit(destUser.getCredit() - money);
        receipt.setPaid(true);
        userRepository.save(destUser);
        receiptRepository.save(receipt);
        sendToClient("done successfully");
    }

    public void deposit(Receipt receipt) throws IOException {
        User sourceUser = getUserByAccountId(receipt.getSourceId());
        long money = receipt.getMoney();
        if (sourceUser == null) {
            sendToClient("invalid account id");
            return;
        }
        sourceUser.setCredit(sourceUser.getCredit() + money);
        receipt.setPaid(true);
        userRepository.save(sourceUser);
        receiptRepository.save(receipt);
        sendToClient("done successfully");
    }

    public void move(Receipt receipt) throws IOException {
        User sourceUser = getUserByAccountId(receipt.getSourceId());
        User destUser = getUserByAccountId(receipt.getDestId());
        long money = receipt.getMoney();
        if (sourceUser == null || destUser == null) {
            sendToClient("invalid account id");
            return;
        }
        if (sourceUser.getCredit() <= money) {
            sendToClient("source account does not have enough money");
            return;
        }
        sourceUser.setCredit(sourceUser.getCredit() - money);
        destUser.setCredit(destUser.getCredit() + money);
        receipt.setPaid(true);
        userRepository.save(sourceUser);
        userRepository.save(destUser);
        receiptRepository.save(receipt);
        sendToClient("done successfully");
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
            sendToClient(ErrorTypes.token_expired.getErrorMessage());
        } catch (InvalidTokenException e) {
            sendToClient(ErrorTypes.token_is_invalid.getErrorMessage());
        }
    }

    public void exit() {
//todo
    }

    private User getUserByToken(String token) {
        String username = Session.getUsernameByToken(token);
        if (username == null)
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
