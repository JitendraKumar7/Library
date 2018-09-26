package com.library;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.library.multicontactpicker.ContactResult;
import com.library.multicontactpicker.LimitColumn;
import com.library.multicontactpicker.MultiContactPicker;
import com.library.reactivelocation.sample.HomeActivity;
import com.library.reactivelocation.sample.utils.LocationToStringFunc;
import com.library.rxlocation.RxLocation;
import com.library.utility.RxPermissions;

import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "JKS";
    private static final int CONTACT_PICKER_REQUEST = 991;
    private final LocationRequest defaultLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    TextView btnReactiveLocation;
    TextView btnRxLocationPicker;
    TextView btnMultiContactPicker;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnReactiveLocation: {
                startActivity(new Intent(this, HomeActivity.class));
                break;
            }
            case R.id.btnRxLocationPicker: {
                new RxPermissions(this)
                        .request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) {

                                    RxLocation.locationUpdates(MainActivity.this, defaultLocationRequest)
                                            .firstElement()
                                            .subscribe(new Consumer<Location>() {
                                                @Override
                                                public void accept(Location location) throws Exception {
                                                    LocationToStringFunc func = new LocationToStringFunc();
                                                    String strLocation = func.apply(location);
                                                    btnRxLocationPicker.setText(strLocation);
                                                    showLog(location.toString());
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    showLog(throwable.getMessage(), throwable);
                                                }
                                            });
                                } else {
                                    Toast.makeText(MainActivity.this, "Sorry, no demo without permission...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            }
            case R.id.btnMultiContactPicker: {
                new RxPermissions(this)
                        .request(Manifest.permission.READ_CONTACTS)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) {
                                    new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
                                            .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                                            .hideScrollbar(false) //Optional - default: false
                                            .showTrack(true) //Optional - default: true
                                            .searchIconColor(Color.WHITE) //Option - default: White
                                            .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                                            .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                            .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                                            .setTitleText("Select Contacts") //Optional - default: Select Contacts
                                            //.setSelectedContacts("10", "5" / myList) //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                                            .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                                            .limitToColumn(LimitColumn.PHONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                                            .showPickerForResult(CONTACT_PICKER_REQUEST);
                                } else {
                                    Toast.makeText(MainActivity.this, "Sorry, no demo without permission...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            }
        }
    }

    private void showLog(Object message) {
        /**/
        Log.d(TAG, String.valueOf(message));
    }

    void showLog(Object message, Throwable e) {
        /**/
        Log.e(TAG, String.valueOf(message), e);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReactiveLocation = findViewById(R.id.btnReactiveLocation);
        btnRxLocationPicker = findViewById(R.id.btnRxLocationPicker);
        btnMultiContactPicker = findViewById(R.id.btnMultiContactPicker);

        btnReactiveLocation.setOnClickListener(this);
        btnRxLocationPicker.setOnClickListener(this);
        btnMultiContactPicker.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                StringBuilder builder = new StringBuilder();
                String row = "%s&nbsp;&nbsp;%s<br>";

                for (ContactResult result : results) {
                    showLog(result.getDisplayName());
                    showLog(result.getPhoneNumbers().toString());

                    String name = result.getDisplayName();
                    List<String> phone = result.getPhoneNumbers();
                    builder.append(String.format(row, name, phone.toString()));
                }
                btnMultiContactPicker.setText(Html.fromHtml(builder.toString()));

            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }

}
