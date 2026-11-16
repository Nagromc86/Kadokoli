package io.github.nagromc86.kadokoli.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class ParcelsWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        KadoWidgets.draw(c, m, ids, "parcels");
    }
}
