package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class TableData implements Serializable
{
    @SerializedName("table_header")
    @Expose
    private List<TableHeader> tableHeader = null;
    @SerializedName("table_rows")
    @Expose
    private List<TableRow> tableRows = null;
    private final static long serialVersionUID = 8266641277430515483L;

    public List<TableHeader> getTableHeader() {
        return tableHeader;
    }

    public void setTableHeader(List<TableHeader> tableHeader) {
        this.tableHeader = tableHeader;
    }

    public List<TableRow> getTableRows() {
        return tableRows;
    }

    public void setTableRows(List<TableRow> tableRows) {
        this.tableRows = tableRows;
    }

}