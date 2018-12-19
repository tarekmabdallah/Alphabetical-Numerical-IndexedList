package com.gmail.tarekmabdallah91.myindexedlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gmail.tarekmabdallah91.myindexedlist.models.CategoryIndexedList;
import com.gmail.tarekmabdallah91.myindexedlist.models.RowInList;
import com.gmail.tarekmabdallah91.myindexedlist.models.SectionIndexedList;

import java.util.List;

class AlphabetListAdapter extends BaseAdapter {

    private static final int TWO = 2;
    private static final int ONE = 1;
    private static final int ZERO =0;
    private List rows;

    void setRows(List rows) {
        this.rows = rows;
    }

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
        View view = convertView;
        String name;
        int itemViewType = getItemViewType(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view = inflater.inflate(R.layout.row_section, parent, false);
            }
        }
        TextView textView = view != null ? (TextView) view.findViewById(R.id.section_letter_tv) : null;
        if (itemViewType == ZERO) { // Item
            final CategoryIndexedList currentCategory = (CategoryIndexedList) getItem(position);
            name = currentCategory.getCategoryName();
            if (name!= null) {
                textView.setText(name);
            }
            view.setTag(currentCategory);
        } else { // Section
            SectionIndexedList section = (SectionIndexedList) getItem(position);
            name = section.getText();
            textView.setText(name);
        }
        return view;
    }
}
