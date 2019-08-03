package com.example.eddys.danyalrequestone;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;


import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class AdminActivity extends AppCompatPreferenceActivity  {
    private static int deviceNum;
    private static NumberCruncher numCrunch;
    private static boolean intentTrans = false;
    private static int activityCount = 0;



    /*
                         * Helper method to determine if the device has an extra-large screen. For
                         * example, 10" tablets are extra-large.
                         */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setupActionBar();



    }

    @Override
    protected void onStart() {
        Log.e("Activity", "Started");
        activityCount++;
        if (intentTrans == false) {
            numCrunch = (NumberCruncher) getIntent().getParcelableExtra("numCrunch");
            intentTrans = true;
        }

        Log.e("ArrayTest2 ", Double.toString(numCrunch.getMiDevices()[0].getDownPayment()[0]));
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (--activityCount == 0) {
            intentTrans = false;
        };
        super.onStop();
    }





    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                intentTrans = false;
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || MiDeviceEdit.class.getName().equals(fragmentName)
                || Other.class.getName().equals(fragmentName)
                ;
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MiDeviceEdit extends PreferenceFragment   {
        private Preference devices[] = new Preference[5];

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.mi_device_edit);
            setHasOptionsMenu(true);



        }

        @Override
        public void onResume(){
            super.onResume();
            for (int i = 0; i < 5; i++) {
                String key = "device" + Integer.toString(i+1);

                devices[i] =  findPreference(key);

                devices[i].setTitle(numCrunch.getMiDevices()[i].getName());

                devices[i].setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        switch (preference.getKey()) {
                            case "device1":
                                deviceNum = 0;
                                break;
                            case "device2":
                                deviceNum = 1;
                                break;
                            case "device3":
                                deviceNum = 2;
                                break;
                            case "device4":
                                deviceNum = 3;
                                break;
                            case "device5":
                                deviceNum = 4;
                                break;
                        }

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(getId(), new MiDeviceSelection());
                        transaction.addToBackStack(null);
                        transaction.commit();


                        return true;
                    }
                });

            }

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case android.R.id.home:
                    startActivity(new Intent(getActivity(), AdminActivity.class));
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }



    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MiDeviceSelection extends PreferenceFragment   {
        DatabaseReference deviceDb;
        private Preference sync;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            addPreferencesFromResource(R.xml.mi_device_values);
            setHasOptionsMenu(true);


            


            String dbKey = "MiDevice" + Integer.toString(deviceNum);
            deviceDb = FirebaseDatabase.getInstance().getReference().child(dbKey);
            final MiDevice tempDevice = numCrunch.getMiDevices()[deviceNum];

            //Log.e("ArrayTest ", Double.toString(tempDevice.getDownPayment()[0]));
            final EditTextPreference baseCostPref = (EditTextPreference) findPreference("baseCost");
            baseCostPref.setText(Double.toString(tempDevice.getBaseCost()));




            baseCostPref.setSummary(
                    Double.toString(tempDevice.getBaseCost()));

            baseCostPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String strNew = newValue.toString();

                    preference.setSummary(strNew);
                    return true;
                }
            });

            final EditTextPreference namePref = (EditTextPreference) findPreference("name");
            namePref.setText((tempDevice.getName()));

            namePref.setSummary(tempDevice.getName());

            namePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(newValue.toString());

                    return true;
                }
            });




            final EditTextPreference downPayment[] = new EditTextPreference[12];
            for (int i = 0; i < 12; i++) {
                String key = "downPayment" + Integer.toString(i+1);


                downPayment[i] = (EditTextPreference) findPreference(key);


                downPayment[i].setText(Double.toString(tempDevice.getDownPayment()[i]));
                downPayment[i].setSummary(Double.toString(tempDevice.getDownPayment()[i]));

                downPayment[i].setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String strNew = newValue.toString();

                        preference.setSummary(strNew);

                        return true;
                    }
                });
            }



            sync = (Preference) findPreference("syncButton");
            sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String name = namePref.getText();
                    tempDevice.setName(name);
                    double baseCost = Double.parseDouble(baseCostPref.getText());
                    numCrunch.getMiDevices()[deviceNum].setBaseCost(baseCost);
                    ArrayList<Double> downPayments = new ArrayList<Double>(8);


                    for (int i = 0; i < 12; i++) {
                        downPayments.add(Double.parseDouble(downPayment[i].getText()));
                        numCrunch.getMiDevices()[deviceNum].setDownPayment(
                                Double.parseDouble(downPayment[i].getText()), i);
                    }



                    deviceDb.child("name").setValue(name);
                    deviceDb.child("baseCost").setValue(baseCost);
                    deviceDb.child("downPayment").setValue(downPayments);

                    Toast.makeText(getActivity(),
                            "Database successfully updated", Toast.LENGTH_LONG).show();


                    return true;
                }
            });


        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            getFragmentManager().popBackStack();
            return true;
        }
    }

    /*--------------------------------------------------------------------------------------------*/
    //Other Fragment

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class Other extends PreferenceFragment   {
        private Preference sync;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_other);
            setHasOptionsMenu(true);





            final EditTextPreference taxPref = (EditTextPreference) findPreference("tax");

            taxPref.setText(Double.toString(numCrunch.getTax()));
            taxPref.setSummary(Double.toString(numCrunch.getTax()));

            taxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.e("Hmm","Hmm");
                    preference.setSummary((newValue.toString()));
                    return true;
                }
            });




            sync = (Preference) findPreference("syncButton");
            sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DatabaseReference dbOtherRef = FirebaseDatabase.getInstance().getReference().child("Other");

                    double tax = Double.parseDouble(taxPref.getText());
                    dbOtherRef.child("tax").setValue(tax);



                    Toast.makeText(getActivity(),
                            "Database successfully updated", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case android.R.id.home:
                    startActivity(new Intent(getActivity(), AdminActivity.class));
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
