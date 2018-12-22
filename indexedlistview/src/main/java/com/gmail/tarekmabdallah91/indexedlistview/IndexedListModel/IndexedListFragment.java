/*
 * Copyright 2018 tarekmabdallah91@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.tarekmabdallah91.indexedlistview.R;

import java.util.Objects;


@SuppressWarnings("unchecked")
public class IndexedListFragment extends ListFragment {

    private TextView sectionStrip;
    private LinearLayout sideIndex;
    private IndexedListPresenter presenter;
    private IndexedList indexedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_indexed_list, container, false);
        sideIndex = root.findViewById(R.id.sideIndex);
        sectionStrip = root.findViewById(R.id.section_strip);
        root.setBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(indexedList.getResBackgroundColor()));
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPresenter();
        setSectionStrip();
        setListView();
    }

    private void setListView (){
        final ListView listView = getListView();
        listView.setPadding(indexedList.getPaddingLeft(),
                indexedList.getPaddingTop(),
                indexedList.getPaddingRight(),
                indexedList.getPaddingBottom());
        setListByData();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {}

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                try {
                    String letter = presenter.getTopItemName(getListView());
                    setTextToSectionStrip(letter);
                } catch (NullPointerException | ClassCastException e) {
                    Log.e(getClass().getSimpleName(), e.getMessage());
                }
            }
        });
    }

    public void setPresenter() {
        presenter = new IndexedListPresenter(indexedList,sideIndex);
        presenter.setSideIndexListener(new SideIndexListener() {
            @Override
            public void onClickIndexListener(Pair pair) {
                if (null != pair){
                    String letter = (String) pair.first;
                    int indexTop = (int) pair.second;
                    getListView().setSelection(indexTop);
                    setTextToSectionStrip(letter);
                }
            }
        });
    }

    private void setTextToSectionStrip (String letter){
        if (null != letter) sectionStrip.setText(letter);
    }

    private void setSectionStrip(){
        sectionStrip.setTextSize(indexedList.getSectionTextSize());
        sectionStrip.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), indexedList.getResColorStrip()));
        sectionStrip.setBackgroundColor(ContextCompat.getColor(getContext(), indexedList.getResBackgroundColorIdStrip()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        indexedList.getIndexedListListener().onClickListItem(v,position,id);
    }

    private void setListByData() {
        presenter.setRows(getListView(),indexedList.getContacts());
    }

    public void setIndexedList(IndexedList indexedList) {
        this.indexedList = indexedList;
    }
}
