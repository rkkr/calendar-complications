package rkr.calendar.complications;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.complications.ComplicationProviderService;
import android.view.View;
import android.widget.RadioButton;

public class ConfigureSeparatorActivity extends Activity {

    //private static final String TAG = "ConfigureSeparator";
    private static final int[] RADIOS = {R.id.radioButton1,
            R.id.radioButton2,
            R.id.radioButton3,
            R.id.radioButton4};
    private int complicationId;
    //private ComponentName provider;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_seperator);

        complicationId = getIntent().getExtras().getInt(ComplicationProviderService.EXTRA_COMPLICATION_ID);
        //provider = getIntent().getExtras().getParcelable(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT);
        //Log.d(TAG, "Configuring complication: " + complicationId);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        ((RadioButton) findViewById(RADIOS[pref.getInt(complicationId + "_separator", 0)])).setChecked(true);
    }

    public void onRadioButtonClicked(View view) {
        if (((RadioButton) view).isChecked())
            for (int i=0; i<RADIOS.length; i++)
                if (view.getId() == RADIOS[i])
                    pref.edit().putInt(complicationId + "_separator", i).apply();
    }
}
