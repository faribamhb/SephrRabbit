package com.sepehr.sepehr.common.exceptions;

/**
 * Created by fatemeh on 9/5/17.
 */
public enum ErrorCodes {

    UNKNOWN("Unknown error", 1),
    INTERNAL_ERROR("Internal error", 6001),
    NOT_IMPLEMENTED("Method not implemented", 6002),

    BAD_INPUT("Input is not correct", 6011),
    MISSING_PARAM("Some parameters are missing.", 6012),
    BAD_REQUEST("Request Param is not correct", 6013),

    NO_ENTITY("No entity can be found.", 6021),
    DUPLICATE_ENTRY("Duplicate entry", 6022),
    PROPERTY_REFERENCE_ERROR("Cannot delete because of reference dependency", 6025),

    ACCESS_DENIED("Access is denied.", 6041),
    INACTIVE_USER("OauthUser is not accountStatus.", 6045),

    NO_USER("OauthUser is not found.", 6044),
    MESSAGE_TEMPLATE_NULL("message template is null .", 6046),
    NO_RESPONSE("no response from the provider", 6060),
    DATABASE_CONNECTION_ERROR("Database connectivity error.", 6050),
    VALIDATION_ERROR("Validation error occurred", 6080),
    INVALID_REFRESH_TOKEN("invalid refresh token", 6081),
    CAN_NOT_GET_ANSWER_FROM_AC("can not get answer from agent controller", 6090),
    CAN_NOT_SEND_DATA_TO_ATM_CLIENT("can not sent data to atm client", 6091),
    Method_Not_Allowed("method not allowed error ", 6091),
    Jpa_System("un categorized data access ",6092),
    IOException("IOException occured",6093),
    NULL_MESSAGE( "check the text of your message. it seems to be null.",6094),
    SQLEXCEPTION("sql occured",6095);



    private final String message;

    private final int code;

    ErrorCodes(String message, int code) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCodes find(int errorCode) {
        for (ErrorCodes item : ErrorCodes.values()) {
            if (item.code == errorCode)
                return item;
        }
        return UNKNOWN;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
