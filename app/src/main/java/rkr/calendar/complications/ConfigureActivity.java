/*
 * Copyright (C) 2017 Raimondas Rimkus
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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.complications.ProviderUpdateRequester;
import android.util.Log;
import android.view.View;
import android.support.wearable.complications.ComplicationProviderService;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigureActivity extends Activity {

    private static final String TAG = "Configure";
    private static final HashMap<Integer, String> SWITCHES = new HashMap<Integer, String>() {{
        put(R.id.week, CalendarProvider.WEEK);
        put(R.id.year, CalendarProvider.YEAR);
        put(R.id.month_text, CalendarProvider.MONTH_TEXT);
        put(R.id.month_number, CalendarProvider.MONTH_NUMBER);
        put(R.id.day, CalendarProvider.DAY);
        put(R.id.year_week, CalendarProvider.YEAR_WEEK);
    }};
    private SharedPreferences pref;
    private int complicationId;
    private ComponentName provider;
    private Set<String> selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        complicationId = getIntent().getExtras().getInt(ComplicationProviderService.EXTRA_COMPLICATION_ID);
        provider = getIntent().getExtras().getParcelable(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT);
        Log.d(TAG, "Configuring complication: " + complicationId);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        selection = pref.getStringSet(complicationId + "_selection", new HashSet<String>());

        for (Integer key : SWITCHES.keySet())
            if (selection.contains(SWITCHES.get(key))) {
                ((Switch) findViewById(key)).setChecked(true);
                Log.d(TAG, "Found key: " + SWITCHES.get(key));
            }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final TextView textSeparator = findViewById(R.id.separator);
        final int separator = pref.getInt(complicationId + "_separator", 0);
        if (CalendarProvider.DATE_SEPARATORS[separator].equals(" "))
            textSeparator.setText("Space");
        else
            textSeparator.setText(CalendarProvider.DATE_SEPARATORS[separator]);
    }

    @Override
    protected void onPause() {
        ProviderUpdateRequester requester = new ProviderUpdateRequester(this, provider);
        requester.requestUpdate(complicationId);
        super.onPause();
    }

    public void onSwitchButtonClicked(View view) {
        if (((Switch) view).isChecked()) {
            selection.add(SWITCHES.get(view.getId()));
            if (view.getId() == R.id.month_number) {
                ((Switch) findViewById(R.id.month_text)).setChecked(false);
                selection.remove(SWITCHES.get(R.id.month_text));
            }
            if (view.getId() == R.id.month_text) {
                ((Switch) findViewById(R.id.month_number)).setChecked(false);
                selection.remove(SWITCHES.get(R.id.month_number));
            }
        } else {
            selection.remove(SWITCHES.get(view.getId()));
        }
        for (String item : selection)
            Log.d(TAG, "Saving selection: " + item);
        //.apply() doesn't persist application restarts for StringSet since Android 2.6
        pref.edit().remove(complicationId + "_selection").commit();
        pref.edit().putStringSet(complicationId + "_selection", selection).commit();
    }

    public void onSeparatorClicked(View view) {
        Intent intent = new Intent(this, ConfigureSeparatorActivity.class);
        intent.putExtra(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT, provider);
        intent.putExtra(ComplicationProviderService.EXTRA_COMPLICATION_ID, complicationId);

        this.startActivity(intent);
    }

    public void onDateFormatClicked(View view) {
        Intent intent = new Intent(this, ConfigureDateFormatActivity.class);
        intent.putExtra(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT, provider);
        intent.putExtra(ComplicationProviderService.EXTRA_COMPLICATION_ID, complicationId);

        this.startActivity(intent);
    }
}
