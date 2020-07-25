package bank;

import java.util.*;

public class Session {

    static int tokenCount;
    static int accountNumber;
    static int receiptNumber;
    private static List<String> expiredTokens;
    private static Map<String, String> allTokens; //Token first Username second

    public enum tokenStatus {Invalid, Expired, Fine}

    static {
        tokenCount = 1;
        accountNumber = UserRepository.getLatestAccountNumber() + 1;
        receiptNumber = ReceiptRepository.getLatestReceiptNumber();
        expiredTokens = new ArrayList<>();
        allTokens = new HashMap<>();
    }

    public static synchronized String getToken(String username) {
        String returnValue = "a" + tokenCount;
        allTokens.put(returnValue, username);
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
        else if (!allTokens.keySet().contains(token))
            return tokenStatus.Invalid;
        return tokenStatus.Fine;
    }

    public static String getUsernameByToken(String token) {
        return allTokens.get(token);
    }

    public static int getAccountNumber() {
        return accountNumber++;
    }
    public static int getReceiptNumber() {
        return receiptNumber++;
    }


}
