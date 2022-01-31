package com.ayubo.life.ayubolife.reports.model;

public class DeleteReportObject {

    String enc_id;
    String report_id;
    String report_type;
    String table_id;


    public DeleteReportObject(String enc_id, String report_id, String report_type, String table_id) {
        this.enc_id = enc_id;
        this.report_id = report_id;
        this.report_type = report_type;
        this.table_id = table_id;
    }

    public String getEnc_id() {
        return enc_id;
    }

    public String getReport_id() {
        return report_id;
    }

    public String getReport_type() {
        return report_type;
    }

    public String getTable_id() {
        return table_id;
    }
}
