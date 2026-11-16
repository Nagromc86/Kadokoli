package io.github.nagromc86.kadokoli.widgets;

import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/** setSummary: the app pushes what the widgets show. takeLaunch / "tab" event: which tab a widget tap asked for. */
@CapacitorPlugin(name = "KadoWidgets")
public class KadoWidgetsPlugin extends Plugin {
    private String pendingTab;

    @Override
    public void load() {
        Intent i = getActivity() == null ? null : getActivity().getIntent();
        if (i != null && KadoWidgets.ACTION_TAB.equals(i.getAction())) {
            pendingTab = i.getStringExtra(KadoWidgets.EXTRA_TAB);
            i.setAction(Intent.ACTION_MAIN);
        }
    }

    @Override
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);
        if (intent != null && KadoWidgets.ACTION_TAB.equals(intent.getAction())) {
            JSObject r = new JSObject();
            r.put("tab", intent.getStringExtra(KadoWidgets.EXTRA_TAB));
            notifyListeners("tab", r);
        }
    }

    @PluginMethod
    public void setSummary(PluginCall call) {
        KadoWidgets.save(getContext(), call.getString("json", "{}"));
        call.resolve();
    }

    @PluginMethod
    public void takeLaunch(PluginCall call) {
        JSObject r = new JSObject();
        r.put("tab", pendingTab);
        pendingTab = null;
        call.resolve(r);
    }
}
