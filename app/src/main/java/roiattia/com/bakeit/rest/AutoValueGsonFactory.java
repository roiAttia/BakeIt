package roiattia.com.bakeit.rest;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
abstract class AutoValueGsonFactory implements TypeAdapterFactory {

    // Static factory method to access the package
    static TypeAdapterFactory create() {
        return new AutoValueGson_AutoValueGsonFactory();
    }
}