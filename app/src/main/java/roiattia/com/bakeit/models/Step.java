package roiattia.com.bakeit.models;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by roiat on 20-03-18.
 */

@AutoValue
public abstract class Step implements Parcelable {

    @SerializedName("shortDescription")
    public abstract String shortDescription();
    @SerializedName("description")
    public abstract String description();
    @SerializedName("videoURL")
    public abstract String videoUrl();
    @SerializedName("thumbnailURL")
    public abstract String thumbnailUrl();

    private boolean isChecked;

    public static Step create(String shortDescription, String description,
                              String videoUrl, String thumbnailUrl) {
        return new AutoValue_Step(shortDescription, description, videoUrl, thumbnailUrl);
    }

    public static TypeAdapter<Step> typeAdapter(Gson gson) {
        return new AutoValue_Step.GsonTypeAdapter(gson);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
