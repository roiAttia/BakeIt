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
public abstract class Ingredient implements Parcelable {

    @SerializedName("ingredient")
    public abstract String name();
    @SerializedName("measure")
    public abstract String measure();
    @SerializedName("quantity")
    public abstract double quantity();

    public static Ingredient create(String name, String measure, double quantity) {
        return new AutoValue_Ingredient(name, measure, quantity);
    }

    public static TypeAdapter<Ingredient> typeAdapter(Gson gson) {
        return new AutoValue_Ingredient.GsonTypeAdapter(gson);
    }

    @Override
    public String toString() {
        return name().toLowerCase() + " (" + quantity() + " " + measure() + ")";

    }
}
