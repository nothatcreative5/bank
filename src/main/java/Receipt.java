import java.io.Reader;

public class Receipt {

    private String token;
    private Receipt_type receipt_type;
    private int money;
    private String sourceId;
    private String destId;
    private String description;

    public Receipt(String token, Receipt_type receipt_type, int money, String sourceId, String destId) {
        this.token = token;
        this.receipt_type = receipt_type;
        this.money = money;
        this.sourceId = sourceId;
        this.destId = destId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Receipt_type getReceipt_type() {
        return receipt_type;
    }

    public void setReceipt_type(Receipt_type receipt_type) {
        this.receipt_type = receipt_type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
