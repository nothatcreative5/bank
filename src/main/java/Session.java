public class Session {

    static int tokenCount;
    static int accountNumber;

    static{
        tokenCount = 1;
        accountNumber = UserRepository.getLatestAccountNumber() + 1;
    }

    public static String getToken() {
        String returnValue = "a" + tokenCount;
        tokenCount++;
        return returnValue;
    }

    public static int getAccountNumber() {
        return accountNumber++;
    }


}
