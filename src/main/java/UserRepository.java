import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository implements Repository<User> {

    private List<User> allUsers;

    public UserRepository() {
        allUsers = new ArrayList<User>();
        readFiles();
    }

    public void save(User Object) {

    }


    private File[] loadFolder(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists())
            file.mkdir();
        return file.listFiles();
    }


    private void readFiles() {
        List<File> files =  Arrays.asList(loadFolder("\\database\\users"));
        files.forEach(e -> readEachUser(e));
    }

    private void readEachUser(File file) {
        User user = Gson
    }

}
