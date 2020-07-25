package bank;

public enum ErrorTypes {

    invalid_receipt_type("invalid receipt type"), invalid_money("invalid money"), invalid_parameters_passed("invalid parameters passed"),
    token_is_invalid("token is invalid"), token_expired("token expired"), source_account_id_is_invalid("source account id is invalid"),
    dest_account_id_is_invalid("dest account id is invalid"), equal_source_and_dest_account("equal source and dest account"),
    invalid_account_id("invalid account id"), your_input_contains_invalid_characters("your input contains invalid characters"),
    username_is_taken("username is not available"), password_not_matches("passwords do not match"), invalid_password_username("invalid username or password");

    private String errorMessage;


    ErrorTypes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
