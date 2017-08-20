package rkr.calendar.complications;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ProviderUpdateRequester;

public class ComplicationReceiver extends BroadcastReceiver {

    public static final String TIMER_ACTION = "TIMER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TIMER_ACTION)) {
            int complicationId = intent.getExtras().getInt(ComplicationProviderService.EXTRA_COMPLICATION_ID);
            ComponentName provider = intent.getExtras().getParcelable(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT);

            ProviderUpdateRequester requester = new ProviderUpdateRequester(context, provider);
            requester.requestUpdate(complicationId);
        }
    }
}
