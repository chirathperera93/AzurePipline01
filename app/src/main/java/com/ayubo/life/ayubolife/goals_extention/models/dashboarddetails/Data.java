package com.ayubo.life.ayubolife.goals_extention.models.dashboarddetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data implements Parcelable
{

    @SerializedName("chart_data")
    @Expose
    private ChartData chartData;
    @SerializedName("goals")
    @Expose
    private List<Goal> goals = null;
    @SerializedName("quick_insights")
    @Expose
    private List<QuickInsight> quickInsights = null;
    @SerializedName("recommend_programs")
    @Expose
    private List<RecommendProgram> recommendPrograms = null;
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
            ;

    protected Data(Parcel in) {
        this.chartData = ((ChartData) in.readValue((ChartData.class.getClassLoader())));
        in.readList(this.goals, (Goal.class.getClassLoader()));
        in.readList(this.quickInsights, (QuickInsight.class.getClassLoader()));
        in.readList(this.recommendPrograms, (RecommendProgram.class.getClassLoader()));
    }

    public Data() {
    }

    public ChartData getChartData() {
        return chartData;
    }

    public void setChartData(ChartData chartData) {
        this.chartData = chartData;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<QuickInsight> getQuickInsights() {
        return quickInsights;
    }

    public void setQuickInsights(List<QuickInsight> quickInsights) {
        this.quickInsights = quickInsights;
    }

    public List<RecommendProgram> getRecommendPrograms() {
        return recommendPrograms;
    }

    public void setRecommendPrograms(List<RecommendProgram> recommendPrograms) {
        this.recommendPrograms = recommendPrograms;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(chartData);
        dest.writeList(goals);
        dest.writeList(quickInsights);
        dest.writeList(recommendPrograms);
    }

    public int describeContents() {
        return 0;
    }

}
