package bank;

public class Constants {
    public static int port = 8090;
    public static final String CREATE_ACCOUNT = "^create_account (\\S+) (\\S+) (\\S+) (\\S+) (\\S+)$";
    public static final String GET_TOKEN = "^get_token (\\S+) (\\S+)$";
    public static final String CREATE_RECEIPT = "^create_receipt (\\S+) (move|deposit|withdraw) (\\d+) (-?\\d+) (-?\\d+) (.*)";
    public static final String GET_TRANSACTION = "^get_transactions (\\S+) (\\S+)$";
    public static final String PAY = "^pay (\\d+)";
    public static final String GET_BALANCE = "^get_balance (.*)";
    public static final String EXIT = "^exit$";
}
