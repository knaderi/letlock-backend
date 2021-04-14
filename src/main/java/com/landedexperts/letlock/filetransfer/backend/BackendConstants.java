package com.landedexperts.letlock.filetransfer.backend;

public class BackendConstants {
    public static final String USER_ID = "user.id"; 
    public static final String DEFAULT_REMOTE_STORAGE = "S3";
    public static final String AUTH_SETTINGS_FILE_NAME = "auth/auth-settings.json";
    public static final long INSTALLERS_CACHE_RESET_INTERVAL = 1000 * 60 * 60; // interval in milliseconds = 1 hour
    
    // Product names should match the names defined in DB product.tp_product_type
    public static final String FILE_TRANSFER_PRODUCT_NAME = "file_transfer";
    public static final String LITE_FILE_TRANSFER_PRODUCT_NAME = "file_transfer_lite";

}
