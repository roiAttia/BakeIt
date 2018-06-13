package roiattia.com.bakeit.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import roiattia.com.bakeit.R;
import roiattia.com.bakeit.models.Ingredient;
import roiattia.com.bakeit.ui.RecipeActivity;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteFactory(getApplicationContext());
    }

    private class RecipeRemoteFactory implements RemoteViewsFactory {

        Context mContext;
        List<Ingredient> mIngredientList;

        public RecipeRemoteFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mIngredientList = RecipeActivity.mIngredients;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mIngredientList != null ? mIngredientList.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Ingredient ingredient = mIngredientList.get(position);
            NumberFormat nf = new DecimalFormat("##.###");
            double quantity = ingredient.quantity();
            // set the ingredient name, quantity and measurement and make it lower case
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);
            remoteViews.setTextViewText(R.id.tv_widget_ingredient,String.format("%s %s %s", nf.format(quantity),
                    ingredient.measure().toLowerCase(),
                    ingredient.name().toLowerCase()));
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
