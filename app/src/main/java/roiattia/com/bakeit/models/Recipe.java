package roiattia.com.bakeit.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by roiat on 20-03-18.
 */

@AutoValue
public abstract class Recipe implements Parcelable {

    @SerializedName("name")
    public abstract String name();
    @SerializedName("ingredients")
    public abstract List<Ingredient> ingredients();
    @SerializedName("steps")
    public abstract List<Step> steps();
    @SerializedName("servings")
    public abstract int servings();
    @SerializedName("image")
    public abstract String image();

    public static Recipe create(String name, List<Ingredient> ingredients, List<Step> steps,
                                    int servings, String image) {
        return new AutoValue_Recipe(name, ingredients, steps, servings, image);
    }

    public static TypeAdapter<Recipe> typeAdapter(Gson gson) {
        return new AutoValue_Recipe.GsonTypeAdapter(gson);
    }
}
