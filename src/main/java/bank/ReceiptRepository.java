package bank;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ReceiptRepository implements Repository<Receipt> {

    private Map<Receipt, File> allReceipts;
    private static ReceiptRepository instance;
    private static int receiptNumber = 1;


    public static ReceiptRepository getInstance() throws IOException {
        if (instance == null)
            return new ReceiptRepository();
        else
            return instance;
    }

    private ReceiptRepository() throws IOException {
        allReceipts = new HashMap<>();
        readFiles();
        this.instance = this;
    }

    @Override
    public synchronized void save(Receipt receipt) throws IOException {
        Gson gson = new Gson();
        if (getReceiptById(receipt.getReceiptId()) != null) {
            Files.writeString(allReceipts.get(receipt).toPath(), gson.toJson(receipt));
        } else {
            File file = new File("database\\receipts\\" + receipt.getReceiptId());
            Files.writeString(file.toPath(), gson.toJson(receipt));
            allReceipts.put(receipt, file);
        }
    }


    private File[] loadFolder(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists())
            file.mkdir();
        return file.listFiles();
    }


    private void readFiles() throws IOException {
        File[] fileArray = loadFolder("database\\receipts");
        List<File> files;
        if (fileArray == null)
            files = new ArrayList<>();
        else
            files = Arrays.asList(fileArray);
        Gson gson = new Gson();
        if (files.size() != 0)
            receiptNumber = gson.fromJson(Files.readString(files.get(files.size() - 1).toPath()), Receipt.class).getReceiptId() + 1;
        files.forEach(e -> {
            try {
                readEachUser(e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public String getTransaction(User user, String transactionType) {
        switch (transactionType) {
            case "+":
                return destTransaction(user);
            case "-":
                return sourceTransaction(user);
            case "*":
                return getAllTransactions(user);
            default:
                return getBasedOnNumber(transactionType);
        }
    }

    private String getBasedOnNumber(String transactionType) {
        int id = Integer.parseInt(transactionType);
        Gson gson = new Gson();
        for (Receipt receipt : allReceipts.keySet()) {
            if (receipt.getReceiptId() == id)
                return gson.toJson(receipt);
        }
        return null;
    }

    private String getAllTransactions(User user) {
        String source = sourceTransaction(user);
        String dest = destTransaction(user);
        if (source.length() > 0 && dest.length() > 0) {
            return source + "*" + dest;
        }
        return source + dest;

    }

    private String sourceTransaction(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        for (Receipt receipt : allReceipts.keySet()) {
            if (user.getAccountNumber() == receipt.getSourceId()) {
                stringBuilder.append(gson.toJson(receipt));
                stringBuilder.append("*");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    private String destTransaction(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        for (Receipt receipt : allReceipts.keySet()) {
            if (user.getAccountNumber() == receipt.getDestId()) {
                stringBuilder.append(gson.toJson(receipt));
                stringBuilder.append("*");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public Receipt getReceiptById(int id) {
        for (Receipt receipt : allReceipts.keySet()) {
            if (receipt.getReceiptId() == id)
                return receipt;
        }
        return null;
    }


    private void readEachUser(File file) throws IOException {
        Gson gson = new Gson();
        Receipt receipt = gson.fromJson(Files.readString(file.toPath()), Receipt.class);
        allReceipts.put(receipt, file);
    }

    public static int getLatestReceiptNumber() {
        return receiptNumber;
    }
}
