package com.example.tomreaddle.kundalik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.tomreaddle.kundalik.DB.DBHelper;
import com.example.tomreaddle.kundalik.entity.note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView add , lock;
    TextView toolbar_title;
    Animation anim_title , anim_add , anim_lock , anim_nav_bot , anim_rec;
    DBHelper dbHelper;
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sPref;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    NoteRecAdd();
                    return true;
                case R.id.navigation_dashboard:
                    TimesAdd();
                    return true;
                case R.id.navigation_notifications:
                    SettingsAdd();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipeRefresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NoteRecAdd();
                swipeRefreshLayout.setColorScheme(R.color.swipe);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        add = findViewById(R.id.add);
        lock = findViewById(R.id.lock);
        toolbar_title = findViewById(R.id.toolbar_title);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_note();
            }
        });
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                if(sPref.getBoolean("lockState" , false)) {
                    lock.setImageResource(R.drawable.unlock);
                    ed.putBoolean("lockState", false);
                } else {
                    if(!sPref.getBoolean("isLock" , false)) {
                        Toast.makeText(MainActivity.this, "Avval parol qo`yish kerak", Toast.LENGTH_SHORT).show();
                        SettingsAdd();
                    } else {
                    ed.putBoolean("lockState", true);
                    lock.setImageResource(R.drawable.lock);
                    Toast.makeText(MainActivity.this, "Bloklandi", Toast.LENGTH_SHORT).show();}
                }
                ed.commit();
            }
        });

        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("check", false);
        ed.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        anim_nav_bot = AnimationUtils.loadAnimation(this , R.anim.anim_nav_bot);
        navigation.setAnimation(anim_nav_bot);

        sPref = getPreferences(MODE_PRIVATE);
        Toast.makeText(this,sPref.getString("key" , ""), Toast.LENGTH_SHORT).show();

        NoteCreate();
        animator();
        checkLock();
    }

    public void animator(){
        anim_title = AnimationUtils.loadAnimation(this , R.anim.anim_toolbar_title);
        anim_add = AnimationUtils.loadAnimation(this , R.anim.anim_toolbar_add);
        anim_lock = AnimationUtils.loadAnimation(this , R.anim.anim_toolbar_lock);
        anim_nav_bot = AnimationUtils.loadAnimation(this , R.anim.anim_nav_bot);
        anim_rec = AnimationUtils.loadAnimation(this , R.anim.anim_rec);

        toolbar_title.setAnimation(anim_title);
        add.setAnimation(anim_add);
        lock.setAnimation(anim_lock);

    }

    public void add_note(){

        dbHelper = new DBHelper(this);
        LayoutInflater inflater =LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.add_note , null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.BOTTOM);
        alert.show();

        final TextView description , title , ok;
        final ImageView image;
        final Spinner spinner;
        final String[] emoji_s = new String[1];
        final String[] feeling_s = new String[1];
        final String emoji[] = {"❌" , "😂" , "😄" , "🤗" , "🤔" , "🙄" , "😮" , "😲" ,"😥" , "😪", "😠" , "💓" , "🎓" , "💤"};
        final String feeling[] = {"Kayfiyat belgilanmadi" , "Kulguli" , "Kayfiyati a'lo" , "Mamnun" , "O'ychan" , "Ensa" ,
                "Taajubda" , "Hayron" , "Xafa" , "Qayg'uli" , "Jahldor" , "Sevib qolgan" , "O'qish" , "Uyqusiragan"};
        title = v.findViewById(R.id.title_add);
        description = v.findViewById(R.id.description_add);
        ok = v.findViewById(R.id.ok_add);
        spinner = v.findViewById(R.id.spinner);

        ArrayAdapter arrayAdapter = new ArrayAdapter(v.getContext() , android.R.layout.browser_link_context_header, emoji);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                emoji_s[0] = emoji[position];
                feeling_s[0] = feeling[position];
                Toast.makeText(MainActivity.this, spinner.getSelectedItemPosition() + feeling[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                note note = new note(title.getText().toString() , emoji_s[0],feeling_s[0], description.getText().toString() , sdf.format(calendar.getTime()));
                dbHelper.note_enter(note);

                alert.cancel();
                Toast.makeText(getApplicationContext(), " Qayd qo'shildi:\n" +
                        emoji_s[0] + feeling_s[0], Toast.LENGTH_SHORT).show();
                NoteRecAdd();
            }
        });

    }

    public void checkLock(){

        sPref = getPreferences(MODE_PRIVATE);
        boolean lockState = sPref.getBoolean("lockState", false);
        boolean lockState_settings = sPref.getBoolean("isLock", false);

        if(lockState_settings) {
            if (!lockState) {
                lock.setImageResource(R.drawable.unlock);
            } else {
                lock.setImageResource(R.drawable.lock);
                get_lock();
            }
        } else
            lock.setImageResource(R.drawable.unlock);
    }

    public void get_lock(){
        LayoutInflater inflater =LayoutInflater.from(this);
        final View v = inflater.inflate(R.layout.lock_screen , null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.getWindow().setGravity(Gravity.BOTTOM);
        alert.show();

        final TextView lock_error;
        final PatternLockView patternLockView;

        lock_error = v.findViewById(R.id.lock_error);
        lock_error.setVisibility(View.INVISIBLE);
        patternLockView = v.findViewById(R.id.lockview);

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            String key;
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                key = PatternLockUtils.patternToString(patternLockView, pattern);
                if (key.equals(sPref.getString("key", null))){
                    alert.cancel();
                    Toast.makeText(MainActivity.this, key + " open", Toast.LENGTH_SHORT).show();
                } else {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
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

    public void NoteCreate(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        recyclerFragment fragment = new recyclerFragment();
        fragmentTransaction.add(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }

    public void NoteRecAdd(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        recyclerFragment fragment = new recyclerFragment();
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }

    public void SettingsAdd(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        settingsFragment fragment = new settingsFragment();
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }

    public void TimesAdd(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        timesFragment fragment = new timesFragment();
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }
}