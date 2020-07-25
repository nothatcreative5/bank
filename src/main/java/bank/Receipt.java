package bank;

public class Receipt {

    private Receipt_type receipt_type;
    private long money;
    private int sourceId;
    private int destId;
    private String description;
    private int receiptId;
    private boolean paid;

    public Receipt(Receipt_type receipt_type, long money, int sourceId, int destId, String description) {
        this.receipt_type = receipt_type;
        this.money = money;
        this.sourceId = sourceId;
        this.destId = destId;
        this.description = description;
        paid = false;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Receipt_type getReceipt_type() {
        return receipt_type;
    }

    public void setReceipt_type(Receipt_type receipt_type) {
        this.receipt_type = receipt_type;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getDestId() {
        return destId;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReceiptId() {
        return this.receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }
}
