package bank;

public class Receipt {

    private Receipt_type receipt_type;
    private long money;
    private String sourceId;
    private String destId;
    private String description;
    private boolean paid ;

    public Receipt(Receipt_type receipt_type, long money, String sourceId, String destId, String description) {
        this.receipt_type = receipt_type;
        this.money = money;
        this.sourceId = sourceId;
        this.destId = destId;
        this.description = description;
        paid = false;
    }
    public boolean getPaid(){
        return paid;
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
