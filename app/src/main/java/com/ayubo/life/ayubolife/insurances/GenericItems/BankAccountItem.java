package com.ayubo.life.ayubolife.insurances.GenericItems;

public class BankAccountItem {

    private String bAccHolderName;
    private String bBankName;
    private String bAccountNo;

    public BankAccountItem(String bAccHolderName, String bBankName, String bAccountNo) {
        this.bAccHolderName = bAccHolderName;
        this.bBankName = bBankName;
        this.bAccountNo = bAccountNo;
    }

    public String getAccHolderName() {
        return bAccHolderName;
    }

    public String getBankName() {
        return bBankName;
    }

    public String getAccountNo() {
        return bAccountNo;
    }
}
