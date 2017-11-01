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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.view.View;
import android.widget.RadioButton;

public class ConfigureDateFormatActivity extends Activity {

    //private static final String TAG = "ConfigureSeparator";
    private static final int[] RADIOS = {R.id.radioButton1,
            R.id.radioButton2,
            R.id.radioButton3,
            R.id.radioButton4,
            R.id.radioButton5,
            R.id.radioButton6};
    private int complicationId;
    //private ComponentName provider;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_date_format);

        complicationId = getIntent().getExtras().getInt(ComplicationProviderService.EXTRA_COMPLICATION_ID);
        //provider = getIntent().getExtras().getParcelable(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT);
        //Log.d(TAG, "Configuring complication: " + complicationId);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        ((RadioButton) findViewById(RADIOS[pref.getInt(complicationId + "_date_format", 0)])).setChecked(true);
    }

    public void onRadioButtonClicked(View view) {
        if (((RadioButton) view).isChecked())
            for (int i=0; i<RADIOS.length; i++)
                if (view.getId() == RADIOS[i])
                    pref.edit().putInt(complicationId + "_date_format", i).apply();
    }
}
