package io.github.nagromc86.kadokoli.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import org.json.JSONObject;

/** Draws both home-screen widgets from the summary the app pushes (gifts total, parcels waiting). */
public final class KadoWidgets {
    static final String ACTION_TAB = "io.github.nagromc86.kadokoli.OPEN_TAB";
    static final String EXTRA_TAB = "kk_tab";

    private KadoWidgets() {}

    static SharedPreferences prefs(Context c) {
        return c.getApplicationContext().getSharedPreferences("kk_widgets", Context.MODE_PRIVATE);
    }

    static void save(Context c, String json) {
        prefs(c).edit().putString("summary", json == null ? "{}" : json).apply();
        refresh(c);
    }

    static void refresh(Context c) {
        try {
            AppWidgetManager m = AppWidgetManager.getInstance(c);
            draw(c, m, m.getAppWidgetIds(new ComponentName(c, GiftsWidget.class)), "gifts");
            draw(c, m, m.getAppWidgetIds(new ComponentName(c, ParcelsWidget.class)), "parcels");
        } catch (Exception ignored) {
        }
    }

    static void draw(Context c, AppWidgetManager m, int[] ids, String kind) {
        if (ids == null || ids.length == 0) return;
        boolean gifts = "gifts".equals(kind);
        RemoteViews v = new RemoteViews(c.getPackageName(), gifts ? R.layout.kk_widget_gifts : R.layout.kk_widget_parcels);
        JSONObject o = null;
        try {
            o = new JSONObject(prefs(c).getString("summary", "{}")).optJSONObject(kind);
        } catch (Exception ignored) {
        }
        String label = o == null ? (gifts ? "À dépenser" : "Colis en attente") : o.optString("label");
        String big = o == null ? "–" : o.optString("big");
        String sub = o == null ? "Ouvre Kadokoli une fois" : o.optString("sub");
        v.setTextViewText(R.id.kk_label, label);
        v.setTextViewText(R.id.kk_big, big);
        v.setTextViewText(R.id.kk_sub, sub);
        v.setViewVisibility(R.id.kk_sub, TextUtils.isEmpty(sub) ? View.GONE : View.VISIBLE);
        Intent launch = c.getPackageManager().getLaunchIntentForPackage(c.getPackageName());
        if (launch != null) {
            // a distinct action so an app already open receives it (onNewIntent) and switches tab
            Intent i = new Intent(ACTION_TAB).setComponent(launch.getComponent()).putExtra(EXTRA_TAB, kind)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            v.setOnClickPendingIntent(R.id.kk_root, PendingIntent.getActivity(c, gifts ? 11 : 12, i,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));
        }
        m.updateAppWidget(ids, v);
    }
}
