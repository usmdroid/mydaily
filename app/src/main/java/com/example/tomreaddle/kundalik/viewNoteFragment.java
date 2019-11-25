package com.example.tomreaddle.kundalik;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tomreaddle.kundalik.DB.DBHelper;
import com.example.tomreaddle.kundalik.entity.note;

@SuppressLint("ValidFragment")
public class viewNoteFragment extends Fragment {

    View view;
    Animation animation;
    ImageView edit , delete , done;
    EditText title , description;
    String title_s , description_s , time_s , emoji_s , feeling_s;
    int id;
    DBHelper dbHelper;

    @SuppressLint("ValidFragment")
    public viewNoteFragment(int id , String title , String emoji , String feeling , String description , String time ){
        this.id = id;
        title_s = title;
        emoji_s = emoji;
        feeling_s = feeling;
        description_s = description;
        time_s = time;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_note_fragment, container , false);
        title = view.findViewById(R.id.view_title);
        description = view.findViewById(R.id.view_discription);
        edit = view.findViewById(R.id.view_edit);
        delete = view.findViewById(R.id.view_delete);
        done = view.findViewById(R.id.view_done);
        title.setText(title_s);
        description.setText(description_s);

        animation = AnimationUtils.loadAnimation(view.getContext() , R.anim.anim_toolbar_title);
        view.setAnimation(animation);
        dbHelper = new DBHelper(view.getContext());

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delete(id);
                rec();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note note = new note(id , title.getText().toString() , emoji_s , feeling_s , description.getText().toString() , time_s);
                dbHelper.update(note);
                if(dbHelper.update(note)!= 0){
                    rec();
                    Toast.makeText(v.getContext(), "O'zgartirish kritildi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void edit(){
        delete.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        done.setVisibility(View.VISIBLE);
    }

    public void rec(){
        FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        recyclerFragment fragment = new recyclerFragment();
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.commit();
    }
}
