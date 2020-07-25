package bank;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ReceiptRepository implements Repository<Receipt> {

    private Map<Receipt, File> allReceipts;
    private static ReceiptRepository instance;
    private static int receiptNumber;


    public static ReceiptRepository getInstance() throws IOException {
        if(instance == null)
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
        if (allReceipts.keySet().contains(receipt)) {
            Files.writeString(Paths.get(allReceipts.get(receipt) + "\\" + receipt.getReceiptId()), gson.toJson(receipt));
        } else {
            File file = new File("\\database\\users");
            Files.writeString(Paths.get(file.toPath() + "\\" + receipt.getReceiptId()), gson.toJson(receipt));
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
        List<File> files =  Arrays.asList(loadFolder("\\database\\receipts"));
        Gson gson = new Gson();
        receiptNumber = gson.fromJson(Files.readString(files.get(files.size() - 1).toPath()), Receipt.class).getReceiptId() + 1;
        files.forEach(e -> {
            try {
                readEachUser(e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public String getTransaction(User user , String transactionType) {
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
            if(receipt.getReceiptId() == id)
                return gson.toJson(receipt);
        }
        return null;
    }

    private String getAllTransactions(User user) {
        return sourceTransaction(user) + destTransaction(user);
    }

    private String sourceTransaction(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        for (Receipt receipt : allReceipts.keySet()) {
            if(user.getAccountNumber() == receipt.getSourceId()) {
                stringBuilder.append(gson.toJson(receipt));
                stringBuilder.append("*");
            }
        }
        return stringBuilder.toString();
    }

    private String destTransaction(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        for (Receipt receipt : allReceipts.keySet()) {
            if(user.getAccountNumber() == receipt.getDestId()) {
                stringBuilder.append(gson.toJson(receipt));
                stringBuilder.append("*");
            }
        }
        return stringBuilder.toString();
    }


    private void readEachUser(File file) throws IOException {
        Gson gson = new Gson();
        Receipt receipt = gson.fromJson(Files.readString(file.toPath()), Receipt.class);
        allReceipts.put(receipt, file);
    }
}
