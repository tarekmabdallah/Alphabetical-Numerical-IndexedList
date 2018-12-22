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

package com.gmail.tarekmabdallah91.myindexedlist.IndexedListModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.tarekmabdallah91.myindexedlist.R;
import com.gmail.tarekmabdallah91.myindexedlist.models.ItemIndexedList;
import com.gmail.tarekmabdallah91.myindexedlist.models.Contact;
import com.gmail.tarekmabdallah91.myindexedlist.models.RowInList;
import com.gmail.tarekmabdallah91.myindexedlist.models.SectionIndexedList;
import com.gmail.tarekmabdallah91.myindexedlist.models.TempIndexItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

class IndexedListPresenter {

    static final int ZERO =0;
    static final int ONE = 1;
    static final int TWO = 2;
    private static final String HASH = "#";
    private List rows;
    private List<TempIndexItem> alphabet;
    private List<RowInList> sectionsList;
    private List<String> LIST_LETTERS ;
    private final HashMap<String, Integer> occurrences = new HashMap<>();
    private SideIndexListener sideIndexListener;
    private IndexedListAdapter indexedListAdapter;
    private Context context;
    private List<RowInList> listOfContactsAndSections;
    private LinearLayout sideIndex;
    private IndexedList indexedList;

    IndexedListPresenter(IndexedList indexedList, LinearLayout sideIndex){
        this.indexedList = indexedList;
        this.context = indexedList.getActivity();
        this.sideIndex = sideIndex;
        rows = new ArrayList(); // for sections and items
        setListLetters();
        if (indexedList.isAlphabetical()) setAlphabeticalIndices();
        else setNumericalIndices();
        setSectionsList();
        setIndexedListAdapter();
    }

    private void setListLetters() {
        LIST_LETTERS = new ArrayList<>(Arrays.asList(indexedList.getLIST_INDICES().split(",")));
    }

    private void setIndexedListAdapter() {
        indexedListAdapter = indexedList.getIndexedListAdapter();
        if (null == indexedListAdapter){
            indexedListAdapter = new IndexedListAdapter();
        }
        indexedListAdapter.setIndexedList(indexedList);
    }

    private void setAlphabeticalIndices() {
        alphabet = new ArrayList<>();
        int start = ZERO, end;
        String previousLetter = null;
        TempIndexItem tmpIndexItem;
        final Pattern NUMBERS_PATTERN = Pattern.compile("[0-9]");
        for (int i = ZERO; i < LIST_LETTERS.size(); i++) {
            String firstLetter = LIST_LETTERS.get(i);
            // Group numbers together in the scroller
            if (NUMBERS_PATTERN.matcher(firstLetter).matches()) {
                firstLetter = HASH;
            }
            // If we've changed to a new letter, add the previous letter to the alphabet scroller
            if (previousLetter != null && !firstLetter.equals(previousLetter)) {
                tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK));
                alphabet.add(tmpIndexItem);
            }
            // Check if we need to add a header row
            if (!firstLetter.equals(previousLetter)) {
                rows.add(new SectionIndexedList(firstLetter));
            }
            previousLetter = firstLetter;
        }
        if (previousLetter != null) {
            // Save the last letter
            tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK));
            alphabet.add(tmpIndexItem);
        }
    }

    private void setNumericalIndices (){
        alphabet = new ArrayList<>();
        int start = ZERO, end;
        String previousLetter = null;
        TempIndexItem tmpIndexItem;
        for (int i = ZERO; i < LIST_LETTERS.size(); i++) {
            String firstLetter = LIST_LETTERS.get(i);
            // If we've changed to a new letter, add the previous letter to the alphabet scroller
            if (previousLetter != null && !firstLetter.equals(previousLetter)) {
                end = rows.size() - ONE;
                tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK));
                alphabet.add(tmpIndexItem);
                start = end + ONE;
            }
            // Check if we need to add a header row
            if (!firstLetter.equals(previousLetter)) {
                rows.add(new SectionIndexedList(firstLetter));
            }
            previousLetter = firstLetter;
        }
        if (previousLetter != null) {
            // Save the last letter
            tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK));
            alphabet.add(tmpIndexItem);
        }
    }

    void setRows(ListView listView,List rows) {
        this.rows = rows;
        indexedListAdapter.setRows(getListOfContactsAndSections(rows));
        listView.setAdapter(indexedListAdapter);
        updateSideIndex();
    }

    private Pair scrollToSelectedSection(String letter) {
        try {
            if (!sectionsList.isEmpty()) {
                SectionIndexedList sectionIndexedList = new SectionIndexedList(letter);
                for (int i = ZERO; i < listOfContactsAndSections.size(); i++) {
                    if (listOfContactsAndSections.get(i) instanceof SectionIndexedList) {
                        SectionIndexedList indexedList = (SectionIndexedList) listOfContactsAndSections.get(i);
                        if (sectionIndexedList.getText().equals(indexedList.getText())) {
                            return new Pair(sectionIndexedList.getText(), i + ONE);
                        }
                    }
                }
            }
        } catch (NullPointerException ignored) {}
        return null;
    }

    private void setSideIndex(){
        int resColorId;
        int paddingValue = (int) context.getResources().getDimension(R.dimen.space4);
        for (int i = ZERO; i <= alphabet.size()- ONE; i++) {
            TempIndexItem tmpIndexItem = alphabet.get(i);
            final String tmpLetter = tmpIndexItem.getLetter();
            TextView tmpTV = new TextView(context);
            tmpTV.setText(tmpLetter);
            tmpTV.setPadding(paddingValue,ZERO,paddingValue,ZERO);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextSize(setTextScaleInPx(indexedList.getItemsSizeSideIndex()));
            if (hasChildes(tmpIndexItem)) {
                resColorId = indexedList.getResColorIdForUnDimmedItems();
            } else {
                resColorId = indexedList.getResColorIdForDimmedItems();
            }
            tmpTV.setTextColor(ContextCompat.getColor(context, resColorId));
            tmpTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Pair pair = scrollToSelectedSection(tmpLetter);
                   sideIndexListener.onClickIndexListener(pair);
                }
            });
            sideIndex.addView(tmpTV);
        }
    }

    private void updateSideIndex() {
        sideIndex.removeAllViews();
        setSideIndex();
    }

    private boolean hasChildes(TempIndexItem tempIndexItem) {
        try {
            String letter = tempIndexItem.getLetter();
            int occurrence = occurrences.get(letter);
            return occurrence > ZERO;
        }catch (NullPointerException e){
            return false;
        }
    }

    private void setSectionsList() {
        sectionsList = new ArrayList<>();
        for (String letter : LIST_LETTERS) {
            SectionIndexedList section = new SectionIndexedList(letter);
            sectionsList.add(section);
        }
    }

    private List getListOfContactsAndSections(List contactsList) {
        final Pattern NON_LETTER_PATTERN = Pattern.compile("[^a-zA-Z]");
        listOfContactsAndSections = new ArrayList<>();
        List<RowInList> tempList = new ArrayList<>();
        List<RowInList> hashSectionChildes = new ArrayList<>();
        final int contactListSize = contactsList.size();
        String firstLetterInSection;
        int index = ZERO;
        // loop for all items in section list to add each section then add it's items in a tempList
        for (RowInList section : sectionsList) {
            firstLetterInSection = ((SectionIndexedList) section).getText().substring(ZERO, ONE);
            int occurrence = ZERO;
            tempList.add(section);
            for (int i = ZERO; i < contactListSize ; i++) {
               try {
                   Contact contact = (Contact) contactsList.get(ZERO);
                   String contactFirstLetter = contact.getName().substring(ZERO, ONE);
                   ItemIndexedList item = new ItemIndexedList(contact.getName(), ZERO);
                   if (indexedList.isAlphabetical() && NON_LETTER_PATTERN.matcher(contactFirstLetter).matches()){
                       // it is not letter - so add it under the # section
                       hashSectionChildes.add(item);
                       contactsList.remove(ZERO);
                   }else {
                       if (firstLetterInSection.equals(contactFirstLetter)) { // it is letter
                           ++occurrence;
                           tempList.add(item);
                           contactsList.remove(ZERO);
                       }
                   }
               }catch (RuntimeException ignored){
                   // maybe catch that exception many times, depending on the num of contacts in each section - ignore it
               }
            }
            occurrences.put(firstLetterInSection, occurrence);
        }
        // move all items to the listOfContactsAndSections which will be passed to the adapter - the hash section in the first if it isn't empty
        if (!hashSectionChildes.isEmpty()){
            hashSectionChildes.add(ZERO, new SectionIndexedList(HASH));
            alphabet.add(ZERO, new TempIndexItem(HASH));
            for (int i = ZERO; i < hashSectionChildes.size() ; i++) {
                try{
                    ItemIndexedList item = (ItemIndexedList) hashSectionChildes.get(i);
                    item.setIndex(index++);
                    listOfContactsAndSections.add(item);
                }catch (ClassCastException ignored){
                    SectionIndexedList section = (SectionIndexedList) hashSectionChildes.get(i);
                    listOfContactsAndSections.add(section);
                }
            }
            occurrences.put(HASH,hashSectionChildes.size());
        }
        for (int i = ZERO; i < tempList.size() ; i++) {
            try {
                ItemIndexedList item = (ItemIndexedList) tempList.get(i);
                item.setIndex(index++);
                listOfContactsAndSections.add(item);
            }catch (Exception ignored){ // will catch exceptions about casting SectionIndexedList
                SectionIndexedList section = (SectionIndexedList) tempList.get(i);
                if (occurrences.get(section.getText()) > ZERO) listOfContactsAndSections.add(section);
            }
        }
        return(listOfContactsAndSections);
    }

    String getTopItemName(ListView listView) throws NullPointerException,ClassCastException{
        View view = listView.getChildAt(ZERO);
        ItemIndexedList itemIndexedList = (ItemIndexedList) view.getTag();
        String name = itemIndexedList.getCategoryName();
        return name.substring(ZERO, ONE);
    }

    void setSideIndexListener(SideIndexListener sideIndexListener) {
        this.sideIndexListener = sideIndexListener;
    }

    private int setTextScaleInPx(float size) {
        float fpixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size, getDisplayMetrics(context));
        return Math.round(fpixels);
    }

    private DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

}
