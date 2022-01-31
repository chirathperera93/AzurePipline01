package com.ayubo.life.ayubolife.new_payment;

import com.ayubo.life.ayubolife.BuildConfig;

import static com.ayubo.life.ayubolife.rest.ApiClient.AZURE_BASE_URL_V1;

import lk.payhere.androidsdk.PHConfigs;

/**
 * Created by Chirath Perera on 2021-09-29.
 */
public class NewPaymentConstantsClass {
    public static final Integer PAYHERE_REQUEST = 11010;
    //    public static final String PAYHERE_MERCHANT_ID = BuildConfig.DEBUG ? "1213867" : "212742";
    public static final String PAYHERE_MERCHANT_ID = "212742";
    //    public static final String PAYHERE_MERCHANT_SECRET = BuildConfig.DEBUG ? "8QoqCMIrgFt8m8GPAlCjCv4kpFA83SOxu4jo5KaA3sjV" : "8LNKPksDSYz4ZAFhYgPVw44KBpgShcN084J9bLNui7TI";
    public static final String PAYHERE_MERCHANT_SECRET = "8LNKPksDSYz4ZAFhYgPVw44KBpgShcN084J9bLNui7TI";
    public static final String PAYHERE_PREAPPROVAL_NOTIFY_URL = AZURE_BASE_URL_V1 + "ayubo-payments/v1/preapproval-notify";
    public static final String PAYHERE_PAYMENT_NOTIFY_URL = AZURE_BASE_URL_V1 + "ayubo-payments/v1/payment-notify";
    //    public static final String PAYHERE_BASE_URL = BuildConfig.DEBUG ? PHConfigs.SANDBOX_URL : PHConfigs.LIVE_URL;
    public static final String PAYHERE_BASE_URL = PHConfigs.LIVE_URL;
}



