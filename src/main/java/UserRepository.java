import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository implements Repository<User> {

    private static int accountNumber;
    private List<User> allUsers;

    public UserRepository() throws IOException {
        allUsers = new ArrayList<User>();
        readFiles();
    }

    @Override
    public void save(User user) throws IOException {
        allUsers.add(user);
        File file = new File("\\database\\users");
        Gson gson = new Gson();
        Files.writeString(file.toPath(), gson.toJson(user));
    }


    private File[] loadFolder(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists())
            file.mkdir();
        return file.listFiles();
    }


    private void readFiles() throws IOException {
        List<File> files =  Arrays.asList(loadFolder("\\database\\users"));
        Gson gson = new Gson();
        accountNumber = gson.fromJson(Files.readString(files.get(files.size() - 1).toPath()), User.class).getAccountNumber() + 1;
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
        User user = gson.fromJson(Files.readString(file.toPath()), User.class);
        allUsers.add(user);
    }

    public static int getLatestAccountNumber() {
        return accountNumber;
    }

}
