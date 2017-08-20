package rkr.calendar.complications;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.complications.ProviderUpdateRequester;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.wearable.complications.ComplicationProviderService;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.HashSet;
import java.util.Set;

public class ConfigureActivity extends Activity {

    private static final String TAG = "ConfigureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        final int complicationId = getIntent().getExtras().getInt(ComplicationProviderService.EXTRA_COMPLICATION_ID);
        final ComponentName provider = getIntent().getExtras().getParcelable(ComplicationProviderService.EXTRA_CONFIG_PROVIDER_COMPONENT);
        final Context context = this.getApplicationContext();
        Log.d(TAG, "Configuring complication: " + complicationId);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final Set<String> selection = pref.getStringSet(complicationId + "_selection", new HashSet<String>());

        final Switch switchWeek = findViewById(R.id.week);
        final Switch switchYear = findViewById(R.id.year);
        final Switch switchMonthNumber = findViewById(R.id.month_number);
        final Switch switchMonthText = findViewById(R.id.month_text);
        final Switch switchDay = findViewById(R.id.day);
        switchWeek.setChecked(selection.contains(CalendarProvider.WEEK));
        switchYear.setChecked(selection.contains(CalendarProvider.YEAR));
        switchMonthNumber.setChecked(selection.contains(CalendarProvider.MONTH_NUMBER));
        switchMonthText.setChecked(selection.contains(CalendarProvider.MONTH_TEXT));
        switchDay.setChecked(selection.contains(CalendarProvider.DAY));

        switchMonthNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    switchMonthText.setChecked(false);
            }
        });
        switchMonthText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    switchMonthNumber.setChecked(false);
            }
        });

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection.clear();
                if (switchWeek.isChecked())
                    selection.add(CalendarProvider.WEEK);
                if (switchYear.isChecked())
                    selection.add(CalendarProvider.YEAR);
                if (switchMonthNumber.isChecked())
                    selection.add(CalendarProvider.MONTH_NUMBER);
                if (switchMonthText.isChecked())
                    selection.add(CalendarProvider.MONTH_TEXT);
                if (switchDay.isChecked())
                    selection.add(CalendarProvider.DAY);
                pref.edit().putStringSet(complicationId + "_selection", selection).commit();
                ProviderUpdateRequester requester = new ProviderUpdateRequester(context, provider);
                requester.requestUpdate(complicationId);
                finish();
            }
        });

    }
}
