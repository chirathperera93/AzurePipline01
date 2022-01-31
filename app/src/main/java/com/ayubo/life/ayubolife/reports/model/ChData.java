package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChData implements Serializable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("table_data")
    @Expose
    private TableData tableData;
    @SerializedName("chart_data")
    @Expose
    private List<ChartDatum> chartData = null;
    private final static long serialVersionUID = -3028900108466966351L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TableData getTableData() {
        return tableData;
    }

    public void setTableData(TableData tableData) {
        this.tableData = tableData;
    }

    public List<ChartDatum> getChartData() {
        return chartData;
    }

    public void setChartData(List<ChartDatum> chartData) {
        this.chartData = chartData;
    }

}