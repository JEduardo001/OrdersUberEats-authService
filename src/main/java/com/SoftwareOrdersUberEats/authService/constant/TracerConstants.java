package com.SoftwareOrdersUberEats.authService.constant;


public class TracerConstants {
    public static final String CORRELATION_KEY = "correlationId";
    public static final String CORRELATION_HEADER = "X-Correlation-Id";

    //MESSAGE
    public static final String MESSAGE_SAVE_EVENT = "Save event topic {}";
    public static final String MESSAGE_SEND_EVENT = "Send event id event{}";
    public static final String MESSAGE_UPDATE_AUTH = "Update auth";
    public static final String MESSAGE_SAVE_AUTH = "Auth saved";
    public static final String MESSAGE_CHANGE_STATUS_AUTH = "Change status auth id {}";
    public static final String MESSAGE_UPDATE_ROLE = "Update role ";
    public static final String MESSAGE_SAVE_ROLE = "Update role ";
    public static final String MESSAGE_DATA_VALIDATION_CHANGE_STATUS_USER_ERROR = "Error data validation to change status user";

    //ERROR
    public static final String ERROR_SEND_EVENT = "Error send event";
}
