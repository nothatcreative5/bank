package bank;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UserRepository implements Repository<User> {

    private static int accountNumber;
    // private List<bank.User> allUsers;
    private Map<User, File> allUsers;
    private static UserRepository instance;

    public static UserRepository getInstance() throws IOException {
        if (instance == null)
            return new UserRepository();
        else
            return instance;
    }

    private UserRepository() throws IOException {
        allUsers = new HashMap<>();
        readFiles();
    }


    @Override
    public synchronized void save(User user) throws IOException {
        Gson gson = new Gson();
        if (allUsers.keySet().contains(user)) {
            Files.writeString(Paths.get(allUsers.get(user) + "\\" + user.getAccountNumber()), gson.toJson(user));
        } else {
            File file = new File("\\database\\users");
            Files.writeString(Paths.get(file.toPath() + "\\" + user.getAccountNumber()), gson.toJson(user));
            allUsers.put(user, file);
        }
    }


    private File[] loadFolder(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists())
            file.mkdir();
        return file.listFiles();
    }


    private void readFiles() throws IOException {
        List<File> files = Arrays.asList(loadFolder("\\database\\users"));
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
        allUsers.put(user, file);
    }

    public static int getLatestAccountNumber() {
        return accountNumber;
    }

    public User getUserByUsername(String username) {
        for (User user : allUsers.keySet()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public User getUserByAccountId(int accountId) {
        for (User user : allUsers.keySet()) {
            if (user.getAccountNumber() == accountId)
                return user;
        }
        return null;
    }

}
