import java.io.IOException;

public interface Repository<T> {

    public void save(T Object) throws IOException, UsernameIsTakenException;

}
