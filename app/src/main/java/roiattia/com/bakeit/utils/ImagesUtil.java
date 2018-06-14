package roiattia.com.bakeit.utils;

import roiattia.com.bakeit.R;

public class ImagesUtil {

    private static int[] imagesId = {R.drawable.nutella_pie, R.drawable.brownies,
            R.drawable.yellow_cake, R.drawable.cheesecake};

    public static int getImageId(int position){
        if(position == 0) return imagesId[0];
        if(position == 1) return imagesId[1];
        if(position == 2) return imagesId[2];
        if(position == 3) return imagesId[3];
        return 0;
    }
}
