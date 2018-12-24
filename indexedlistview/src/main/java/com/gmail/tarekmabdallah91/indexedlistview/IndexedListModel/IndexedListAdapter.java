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

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmail.tarekmabdallah91.indexedlistview.R;
import com.gmail.tarekmabdallah91.indexedlistview.models.ItemIndexedList;
import com.gmail.tarekmabdallah91.indexedlistview.models.RowInList;
import com.gmail.tarekmabdallah91.indexedlistview.models.SectionIndexedList;
import java.util.List;
import static com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel.IndexedListPresenter.ONE;
import static com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel.IndexedListPresenter.TWO;
import static com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel.IndexedListPresenter.ZERO;

public class IndexedListAdapter extends BaseAdapter {

    private List rows;
    private IndexedList indexedList;

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public RowInList getItem(int position) {
        return (RowInList) rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return TWO;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof SectionIndexedList) {
            return ONE;
        } else {
            return ZERO;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        View view = convertView;
        String name;
        int itemViewType = getItemViewType(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view = inflater.inflate(R.layout.item_row, parent, false);
            }
        }
        if (view != null){
            TextView textView = view.findViewById(R.id.row_tv);
            if (itemViewType == ZERO) { // Item
                final ItemIndexedList currentCategory = (ItemIndexedList) getItem(position);
                name = currentCategory.getName();
                if (name!= null) {
                    textView.setText(name);
                    textView.setTextSize(indexedList.getItemsTextSize());
                    textView.setTextColor(ContextCompat.getColor(context,indexedList.getResColorItem()));
                    textView.setPadding((int) context.getResources().getDimension(R.dimen.space8),ZERO,ZERO,ZERO);
                }
                view.setTag(currentCategory);
            } else { // Section
                SectionIndexedList section = (SectionIndexedList) getItem(position);
                name = section.getText();
                textView.setText(name);
                textView.setTextSize(indexedList.getSectionTextSize());
                textView.setTextColor(ContextCompat.getColor(context, indexedList.getResColorSection()));
            }
        }
        return view;
    }

    void setRows(List rows) {
        this.rows = rows;
    }

    void setIndexedList(IndexedList indexedList) {
        this.indexedList = indexedList;
    }
}
