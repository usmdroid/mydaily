package com.example.tomreaddle.kundalik;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.TextView;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;

public class settingsFragment extends Fragment {

    Switch lock, theme;
    MainActivity mainActivity = new MainActivity();
    Boolean isLock = false;
    SharedPreferences sPref;
    Context context;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);
        context = view.getContext();
        sPref = this.getActivity().getPreferences(MODE_PRIVATE);
        lock = view.findViewById(R.id.lock_switch);
        theme = view.findViewById(R.id.theme_switch);
        lock.setChecked(sPref.getBoolean("isLock" , false));

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lock.isChecked()) {
                    set_Lock();
                }
                if(!lock.isChecked()){
                    del_Lock();
                }
            }
        });

        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theme.isChecked()) {
                    Toast.makeText(v.getContext(), "belgilandi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "belgilanmadi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lock.setChecked(sPref.getBoolean("isLock" , true));
        return view;
    }

    public void set_Lock(){
        LayoutInflater inflater = LayoutInflater.from(context);
        final View viewLock = inflater.inflate(R.layout.lock_screen , null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(viewLock);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();

        final TextView lock_error;
        final PatternLockView patternLockView;

        lock_error = viewLock.findViewById(R.id.lock_error);
        patternLockView = viewLock.findViewById(R.id.lockview);

        final String[] kalit = new String[1];
        final SharedPreferences.Editor ed = sPref.edit();

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                kalit[0] = PatternLockUtils.patternToString(patternLockView, pattern);
                ed.putString("key" , kalit[0]);
                ed.putBoolean("isLock" , true);
                ed.commit();
                //mainActivity.set_image(R.drawable.lock);
                Toast.makeText(context, kalit[0] + " parol o`rnatildi", Toast.LENGTH_SHORT).show();
                alert.cancel();
            }

            @Override
            public void onCleared() {

            }
        });

    }

    public void del_Lock(){
        LayoutInflater inflater = LayoutInflater.from(context);
        final View viewLock = inflater.inflate(R.layout.lock_screen , null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(viewLock);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();

        final TextView lock_error;
        final PatternLockView patternLockView;
        final String[] kalit = new String[1];

        lock_error = viewLock.findViewById(R.id.lock_error);
        patternLockView = viewLock.findViewById(R.id.lockview);

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                kalit[0] = PatternLockUtils.patternToString(patternLockView, pattern);
                if(sPref.getString("key" , "").equals(kalit[0])){
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("key" , "");
                    ed.putBoolean("isLock" , false);
                    alert.cancel();
                    ed.putBoolean("lockState" , false);
                    ed.commit();
                    Toast.makeText(context, "Parol olib tashlandi", Toast.LENGTH_SHORT).show();
                } else {
                    Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                    if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
                        vibrator.vibrate(VibrationEffect.createOneShot(300 , VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                    else
                        vibrator.vibrate(300);
                    lock_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCleared() {

            }
        });


    }

}