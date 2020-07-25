import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceiptRepository implements Repository<Receipt> {

    private List<Receipt> allReceipts;
    private static ReceiptRepository instance;


    public static ReceiptRepository getInstance() throws IOException {
        if(instance == null)
            return new ReceiptRepository();
        else
            return instance;
    }

    private ReceiptRepository() throws IOException {
        allReceipts = new ArrayList<>();
        readFiles();
        this.instance = this;
    }

    @Override
    public synchronized void save(Receipt receipt) throws IOException {
        allReceipts.add(receipt);
        File file = new File("\\database\\users");
        Gson gson = new Gson();
        Files.writeString(file.toPath(), gson.toJson(receipt));
    }


    private File[] loadFolder(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists())
            file.mkdir();
        return file.listFiles();
    }


    private void readFiles() throws IOException {
        List<File> files =  Arrays.asList(loadFolder("\\database\\receipts"));
        files.forEach(e -> {
            try {
                readEachUser(e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }


    private void readEachUser(File file) throws IOException {
        Gson gson = new Gson();
        Receipt receipt = gson.fromJson(Files.readString(file.toPath()), Receipt.class);
        allReceipts.add(receipt);
    }
}
