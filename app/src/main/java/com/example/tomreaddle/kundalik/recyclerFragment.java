package com.example.tomreaddle.kundalik;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tomreaddle.kundalik.DB.DBHelper;
import com.example.tomreaddle.kundalik.entity.note;
import java.util.List;

public class recyclerFragment extends Fragment {

    RecyclerView recyclerView;
    DBHelper dbHelper;
    private List<note> list_note;
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_fragment, container , false);

        recyclerView = view.findViewById(R.id.recycler_note);
        dbHelper = new DBHelper(view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        list_note = dbHelper.note_list();
        adapter = new MyAdapter(list_note , view.getContext());
        recyclerView.setAdapter(adapter);
        return view;

    }
}
