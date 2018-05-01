package roiattia.com.bakeit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import roiattia.com.bakeit.R;
import roiattia.com.bakeit.models.Ingredient;
import roiattia.com.bakeit.models.Recipe;
import roiattia.com.bakeit.ui.RecipeActivity;
import roiattia.com.bakeit.ui.RecipesListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.appwidget_text, String.format("%s %s", recipe.name(), context.getString(R.string.widget_ingredients)));
        Intent ingredientsIntent = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.appwidget_listview, ingredientsIntent);
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(RecipesListActivity.RECIPE_ITEM, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientWidgets(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateRecipeWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

