package com.ayubo.life.ayubolife.pojo.timeline.main;


        import java.util.List;
        import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class MainResponse implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("goal")
    @Expose
    private List<Goal> goal = null;

    @SerializedName("banner")
    @Expose
    private Banner banner = null;

    @SerializedName("quick_links")
    @Expose
    private List<QuickLink> quickLinks = null;
    @SerializedName("top_tiles")
    @Expose
    private List<TopTile> topTiles = null;
    public final static Parcelable.Creator<MainResponse> CREATOR = new Creator<MainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MainResponse createFromParcel(Parcel in) {
            return new MainResponse(in);
        }

        public MainResponse[] newArray(int size) {
            return (new MainResponse[size]);
        }

    };

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    protected MainResponse(Parcel in) {
        this.result = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.goal, (Goal.class.getClassLoader()));
        in.readList(this.quickLinks, (QuickLink.class.getClassLoader()));
        in.readList(this.topTiles, (TopTile.class.getClassLoader()));
    //    this.data = ((Data) in.readValue((Data.class.getClassLoader())));
      //  in.readValue(this.banner, (Banner.class.getClassLoader()));
        this.banner = ((Banner) in.readValue((Banner.class.getClassLoader())));
    }

    public MainResponse() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<Goal> getGoal() {
        return goal;
    }

    public void setGoal(List<Goal> goal) {
        this.goal = goal;
    }

    public List<QuickLink> getQuickLinks() {
        return quickLinks;
    }

    public void setQuickLinks(List<QuickLink> quickLinks) {
        this.quickLinks = quickLinks;
    }

    public List<TopTile> getTopTiles() {
        return topTiles;
    }

    public void setTopTiles(List<TopTile> topTiles) {
        this.topTiles = topTiles;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeList(goal);
        dest.writeList(quickLinks);
        dest.writeList(topTiles);
        dest.writeValue(banner);
    }

    public int describeContents() {
        return 0;
    }

}



