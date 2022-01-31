package com.ayubo.life.ayubolife.pojo;



        import android.os.Parcel;
        import android.os.Parcelable;
        import android.os.Parcelable.Creator;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class TokenMainResponse implements Parcelable
{

    @SerializedName("userToken")
    @Expose
    private String userToken;
    public final static Parcelable.Creator<TokenMainResponse> CREATOR = new Creator<TokenMainResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TokenMainResponse createFromParcel(Parcel in) {
            return new TokenMainResponse(in);
        }

        public TokenMainResponse[] newArray(int size) {
            return (new TokenMainResponse[size]);
        }

    }
            ;

    protected TokenMainResponse(Parcel in) {
        this.userToken = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TokenMainResponse() {
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userToken);
    }

    public int describeContents() {
        return 0;
    }

}