/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rkr.calendar.complications;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.complications.ComplicationManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.support.wearable.complications.ComplicationText;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomComplicationProviderService extends ComplicationProviderService {

    private static final String TAG = "ComplicationProvider";

    static PendingIntent getConfigurationIntent(Context context, ComponentName provider, int complicationId) {
        Intent intent = new Intent(context, ConfigureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_CONFIG_PROVIDER_COMPONENT, provider);
        intent.putExtra(EXTRA_COMPLICATION_ID, complicationId);

        return PendingIntent.getActivity(context, complicationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /*static PendingIntent getTimerIntent(Context context, int complicationId, ComponentName thisProvider) {
        Intent intent = new Intent(context, ComplicationReceiver.class);
        intent.setAction(ComplicationReceiver.TIMER_ACTION);
        intent.putExtra(ComplicationProviderService.EXTRA_COMPLICATION_ID, complicationId);
        intent.putExtra(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT, thisProvider);
        return PendingIntent.getBroadcast(context, complicationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static void RegisterUpdater(Context context, int complicationId, ComponentName thisProvider) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent timerIntent = getTimerIntent(context, complicationId, thisProvider);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), INTERVAL_DAY, timerIntent);
    }*/

    @Override
    public void onComplicationActivated(int complicationId, int dataType, ComplicationManager complicationManager) {
        Log.d(TAG, "onComplicationActivated(): " + complicationId);

        //ComponentName thisProvider = new ComponentName(this, getClass());
        //RegisterUpdater(this, complicationId, thisProvider);

        /*try {
            getConfigurationIntent(this, new ComponentName(this, getClass()), complicationId).send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "Failed to start configuration activity");
        }*/
    }

    @Override
    public void onComplicationUpdate(int complicationId, int dataType, ComplicationManager complicationManager) {
        Log.d(TAG, "onComplicationUpdate() id: " + complicationId);

        ComponentName thisProvider = new ComponentName(this, getClass());
        PendingIntent complicationPendingIntent = getConfigurationIntent(this, thisProvider, complicationId);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selection = pref.getStringSet(complicationId + "_selection", null);
        if (selection == null)
            selection = new HashSet<>();
        else
            selection = new HashSet<>(selection);
        int separator = pref.getInt(complicationId + "_separator", 0);

        ComplicationData.Builder complicationData = new ComplicationData.Builder(dataType);
        complicationData.setTapAction(complicationPendingIntent);
        List<String> rows = CalendarProvider.GetRows(selection, separator, dataType);

        switch (dataType) {
            case ComplicationData.TYPE_SHORT_TEXT:
                if (rows.isEmpty()) {
                    rows.add("Tap to");
                    rows.add("add");
                }
                complicationData.setShortText(ComplicationText.plainText(rows.get(0)));
                if (rows.size() > 1)
                    complicationData.setShortTitle(ComplicationText.plainText(rows.get(1)));
                break;
            case ComplicationData.TYPE_LONG_TEXT:
                if (rows.isEmpty()) {
                    rows.add("Tap to add");
                }
                complicationData.setLongText(ComplicationText.plainText(rows.get(0)));
                if (rows.size() > 1)
                    complicationData.setLongTitle(ComplicationText.plainText(rows.get(1)));
                break;
            default:
                complicationManager.noUpdateRequired(complicationId);
                if (Log.isLoggable(TAG, Log.WARN)) {
                    Log.w(TAG, "Unexpected complication type " + dataType);
                }
        }

        complicationManager.updateComplicationData(complicationId, complicationData.build());
    }

    @Override
    public void onComplicationDeactivated(int complicationId) {
        Log.d(TAG, "onComplicationDeactivated(): " + complicationId);

        /*SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().remove(complicationId + "_selection").apply();*/

        /*AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        ComponentName thisProvider = new ComponentName(this, getClass());
        PendingIntent timerIntent = getTimerIntent(this, complicationId, thisProvider);
        alarmMgr.cancel(timerIntent);*/
    }
}
