public enum Receipt_type {
    deposit, withdraw, move;

    public static Receipt_type getType(String type) {
        switch (type) {
            case "deposit":
                return deposit;
            case "withdraw":
                return withdraw;
            case "move":
                return move;
            default:
                return null;
        }
    }

    public static String getString(Receipt_type receiptType) {
        switch (receiptType) {
            case deposit:
                return "deposit";
            case move:
                return "move";
            default:
                return "withdraw";
        }
    }
}
