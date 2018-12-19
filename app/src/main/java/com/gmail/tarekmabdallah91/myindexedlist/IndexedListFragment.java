package com.gmail.tarekmabdallah91.myindexedlist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gmail.tarekmabdallah91.myindexedlist.models.ListItem;
import com.gmail.tarekmabdallah91.myindexedlist.models.Section;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class IndexedListFragment extends Fragment {


    private ListView listView;
    private String letters = "A,B,C,D";//,E,F,G,H,Y,Z";
    private List<String> lettersList = new ArrayList<>(Arrays.asList(letters.split(",")));

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_indexed_list, container, false);
        listView = root.findViewById(R.id.list_view);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IndexedAdapter indexedAdapter = new IndexedAdapter(getContext());
        indexedAdapter.addAll(getSections());
        listView.setAdapter(indexedAdapter);
    }

    private List<Section> getSections (){
        List<Section> sections = new ArrayList<>();
        for (int i = 0; i < lettersList.size() ; i++) {
            Section section = new Section(lettersList.get(i),i);
            sections.add(section);
        }
        for (int i = 0; i < 10 ; i++) {
            sections.get(0).getChildes().add(new ListItem("A" + i, i));
        }
        for (int i = 0; i < 10 ; i++) {
            sections.get(1).getChildes().add(new ListItem("B" + i, i));
        }
        for (int i = 0; i < 10 ; i++) {
            sections.get(2).getChildes().add(new ListItem("C" + i, i));
        }
        for (int i = 0; i < 10 ; i++) {
            sections.get(3).getChildes().add(new ListItem("D" + i, i));
        }
//        for (Section section: sections) {
//            for (int j = 0; j < 10; j++) {
//                section.getChildes().add(new ListItem(section.getName() + j, j));
//            }
//        }
        return sections;
    }
}
