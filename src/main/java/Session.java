import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Session {

    static int tokenCount;
    static int accountNumber;
    private static List<String> expiredTokens;
    private static List<String> allTokens;

    private enum tokenStatus {Invalid, Expired, Fine}

    static {
        tokenCount = 1;
        accountNumber = UserRepository.getLatestAccountNumber() + 1;
        expiredTokens = new ArrayList<>();
        allTokens = new ArrayList<>();
    }

    public static synchronized String getToken() {
        String returnValue = "a" + tokenCount;
        allTokens.add(returnValue);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                allTokens.remove(returnValue);
                expiredTokens.add(returnValue);
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, 1 * 3600 * 1000);
        tokenCount++;
        return returnValue;
    }

    public static tokenStatus isTokenExpired(String token) {
        if (expiredTokens.contains(token))
            return tokenStatus.Expired;
        else if (!allTokens.contains(token))
            return tokenStatus.Invalid;
        return tokenStatus.Fine;
    }

    public static int getAccountNumber() {
        return accountNumber++;
    }


}
