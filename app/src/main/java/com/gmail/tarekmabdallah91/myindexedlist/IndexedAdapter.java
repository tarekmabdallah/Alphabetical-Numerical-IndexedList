package com.gmail.tarekmabdallah91.myindexedlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.tarekmabdallah91.myindexedlist.models.ListItem;
import com.gmail.tarekmabdallah91.myindexedlist.models.Section;
import java.util.List;

public class IndexedAdapter extends ArrayAdapter<Section> {

    private List<Section> sections;

    public IndexedAdapter(Context context) {
        super(context, 0);
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Context context = parent.getContext();
        SectionViewHolder holder;
        View root = convertView;
        if (null == root){
            root = LayoutInflater.from(context).inflate(R.layout.section_item, parent,false);
            holder = new SectionViewHolder();
            holder.linearLayout = root.findViewById(R.id.items_layout);
            holder.textView = root.findViewById(R.id.section_label);
            root.setTag(holder);
        }else {
            holder = (SectionViewHolder) root.getTag();
        }
        Section section = getItem(position);
        holder.textView.setText(section.getName());
        List<ListItem> items = section.getChildes();
        for (int i = 0 ; i < items.size() ; i++){
            TextView textView = new TextView(context);
            ListItem item =  items.get(i);
            textView.setText(item.getName());
            holder.linearLayout.addView(textView);
        }
        return root;
    }

    class SectionViewHolder {
        LinearLayout linearLayout;
        TextView textView;
    }
}
