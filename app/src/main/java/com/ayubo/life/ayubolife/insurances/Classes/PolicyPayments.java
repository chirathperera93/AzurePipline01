package com.ayubo.life.ayubolife.insurances.Classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PolicyPayments {

    public PaymentHistoryData[] getPolicyPaymentData(JsonArray policyPaymentHistory) {
        PaymentHistoryData[] data = new PaymentHistoryData[policyPaymentHistory.size()];

        for (int i = 0; i < policyPaymentHistory.size(); i++) {
            PaymentHistoryData row = new PaymentHistoryData();
            JsonElement jsonElement = policyPaymentHistory.get(i);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            try {
                row.policyPaymentId = jsonObject.get("id").getAsString();


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date dateNw = format.parse(jsonObject.get("py_createdatetime").getAsString());
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                row.policyPaymentDate = formatter.format(dateNw);
                row.policyPaymentAmount = jsonObject.get("py_currency").getAsString() + " " + (new BigDecimal(jsonObject.get("py_amount").getAsString()).toString());
                row.policyPaymentStatus = jsonObject.get("py_status").getAsString();
                data[i] = row;
            } catch (Exception e) {
                e.getStackTrace();
            }


        }
        return data;
    }
}
