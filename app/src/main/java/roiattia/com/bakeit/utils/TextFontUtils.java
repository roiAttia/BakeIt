package roiattia.com.bakeit.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextFontUtils {

    public static TextView setTextFont(Context context, String title){
        TextView tv = new TextView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText(title);
        tv.setTextSize(30);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "bilbo.ttf");
        tv.setTypeface(tf, Typeface.BOLD);
        return tv;
    }
}
