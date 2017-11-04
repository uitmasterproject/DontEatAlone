package com.app.donteatalone.views.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static android.app.Activity.RESULT_OK;
import static com.app.donteatalone.views.register.RegisterStep1Fragment.userName;

/**
 * Created by ChomChom on 4/7/2017
 */

public class RegisterStep5Fragment extends Fragment implements PlaceSelectionListener {
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int REQUEST_SELECT_PLACE = 1000;
    private static final String LOG_TAG = "PlaceSelectionListener";
    private EditText edtAddress;
    private View viewGroup;
    private RelativeLayout rlNext, rlClose;
    private ViewPager _mViewPager;
    private LinearLayout llRoot;
    private String location="";

    public static Fragment newInstance() {
        return new RegisterStep5Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewGroup = inflater.inflate(R.layout.fragment_register_step5, null);
        init();
        llRootTouch();
        setEdtAdress();
        clickButtonNextStep();
        rlCloseClick();
        return viewGroup;
    }

    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        edtAddress = (EditText) viewGroup.findViewById(R.id.fragment_register_step5_edt_address);
        edtAddress.requestFocus();
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step5_btn_next);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step5_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step5_ll_root);
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(getActivity());
                return true;
            }
        });
    }
    
    //khoi tao gia tri cho address
    private void setEdtAdress() {
        edtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder
                            (PlaceAutocomplete.MODE_OVERLAY)
                            .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
                            .build(getActivity());
                    startActivityForResult(intent, REQUEST_SELECT_PLACE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //luu lai noi duoc chon
    @Override
    public void onPlaceSelected(Place place) {
        edtAddress.setText(AppUtils.convertStringToNFD(getString(R.string.formatted_place_data, place.getName(), place.getAddress())));
        location=place.getLatLng().toString().substring(10,place.getLatLng().toString().length()-1);
    }

    //kiem tra loi cho noi duoc chon
    @Override
    public void onError(Status status) {
        Toast.makeText(getContext(), "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    //tra ve gia tri khi thoat khoi trang tim kiem dia chi
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                this.onError(status);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clickButtonNextStep() {
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setAddress(edtAddress.getText().toString());
                userName.setLatlngAdress(location);
                _mViewPager.setCurrentItem(5, true);
            }
        });
    }

    private void rlCloseClick(){
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
