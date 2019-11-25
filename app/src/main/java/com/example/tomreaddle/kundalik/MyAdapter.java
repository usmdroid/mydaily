package com.example.tomreaddle.kundalik;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.tomreaddle.kundalik.entity.note;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<note> list;
    private Context mContext;

    public MyAdapter(List<note> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notes , viewGroup , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final note item = list.get(i);
        viewHolder.title.setText(item.getTitle());
        viewHolder.emoji.setText(item.getEmoji());
        viewHolder.feeling.setText(item.getFeeling());
        viewHolder.time.setText(item.getTime());
        viewHolder.description.setText(item.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        TextView emoji;
        TextView feeling;
        TextView description;
        CheckBox delete_check;
        Animation anim_menu , anim_title , anim_discription , anim_time , anim_open;

        public ViewHolder(final View itemView) {
            super(itemView);

            int check;
            title = itemView.findViewById(R.id.title);
            emoji = itemView.findViewById(R.id.emoji);
            feeling = itemView.findViewById(R.id.feeling);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
            delete_check = itemView.findViewById(R.id.delete_check);

            anim_title = AnimationUtils.loadAnimation(itemView.getContext() , R.anim.anim_toolbar_title);
            anim_time = AnimationUtils.loadAnimation(itemView.getContext() , R.anim.anim_nav_bot);
            anim_discription = AnimationUtils.loadAnimation(itemView.getContext() , R.anim.anim_rec);
            anim_menu = AnimationUtils.loadAnimation(itemView.getContext() , R.anim.anim_toolbar_lock);
            anim_open = AnimationUtils.loadAnimation(itemView.getContext() , R.anim.anim_toolbar_add);

            SharedPreferences sPref = ((Activity) mContext).getPreferences(MODE_PRIVATE);
            if(sPref.getBoolean("check" , false)) check = 0; else check = 8;

            delete_check.setVisibility(check);

            if(check == 8) {
                title.setAnimation(anim_title);
                time.setAnimation(anim_time);
                description.setAnimation(anim_discription);
                delete_check.setAnimation(anim_menu);
                emoji.setAnimation(anim_time);
                feeling.setAnimation(anim_time);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final note item = list.get(getPosition());
                        FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        viewNoteFragment view = new viewNoteFragment(item.getId(), item.getTitle(), item.getEmoji() , item.getFeeling() , item.getDescription(), item.getTime());
                        fragmentTransaction.replace(R.id.fragment_frame, view);
                        fragmentTransaction.commit();

                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delete_check.setVisibility(View.VISIBLE);
                        SharedPreferences sPref = ((Activity) mContext).getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putBoolean("check", true);
                        ed.commit();
                        FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        recyclerFragment fragment = new recyclerFragment();
                        fragmentTransaction.replace(R.id.fragment_frame, fragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                });
            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(delete_check.isChecked())
                            delete_check.setChecked(false);
                        else
                            delete_check.setChecked(true);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delete_check.setVisibility(View.GONE);
                        SharedPreferences sPref = ((Activity) mContext).getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putBoolean("check", false);
                        ed.commit();
                        FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        recyclerFragment fragment = new recyclerFragment();
                        fragmentTransaction.replace(R.id.fragment_frame, fragment);
                        fragmentTransaction.commit();
                        return true;
                    }
                });
            }
        }
    }
}
