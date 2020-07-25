package bank;

public class User {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int accountNumber;
    private long credit;

    public User(String username, String password, String firstName, String lastName, long credit) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.firstName = lastName;
        this.credit = credit;
        this.accountNumber = Session.getAccountNumber();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
