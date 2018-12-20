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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.tarekmabdallah91.myindexedlist.models.CategoryIndexedList;
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
    private static final int TWENTY = 20;
    private List rows;
    private List<TempIndexItem> alphabet;
    private List<RowInList> sectionsList;
    private List<String> LIST_LETTERS ;
    private final HashMap<String, Integer> occurrences = new HashMap<>();
    private int indexListSize;
    private AlphabetListAdapter alphabetListAdapter;
    private Context context;
    private List listOfContactsAndSections;
    private LinearLayout sideIndex;
    private IndexedList indexedList;

    IndexedListPresenter(IndexedList indexedList, LinearLayout sideIndex){
        this.indexedList = indexedList;
        this.context = indexedList.getActivity();
        this.sideIndex = sideIndex;
        rows = new ArrayList(); // for sections and items
        setListLetters();
        setAlphabeticalList();
        setSectionsList();
        setAlphabetListAdapter();
    }

    private void setListLetters() {
        LIST_LETTERS = new ArrayList<>(Arrays.asList(indexedList.getLIST_INDICATORS().split(",")));
    }

    private void setAlphabetListAdapter() {
        alphabetListAdapter = new AlphabetListAdapter();
        alphabetListAdapter.setIndexedList(indexedList);
    }

    private void setAlphabeticalList() {
        alphabet = new ArrayList<>();
        int start = ZERO, end;
        String previousLetter = null;
        TempIndexItem tmpIndexItem;
        Pattern numberPattern = Pattern.compile("[0-9]");
        for (int i = ZERO; i < LIST_LETTERS.size(); i++) {
            String firstLetter = LIST_LETTERS.get(i);
            // Group numbers together in the scroller
            if (numberPattern.matcher(firstLetter).matches()) {
                firstLetter = "#";
            }
            // If we've changed to a new letter, add the previous letter to the alphabet scroller
            if (previousLetter != null && !firstLetter.equals(previousLetter)) {
                end = rows.size() - ONE;
                tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK), start, end);
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
            tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK), start, rows.size() - ONE);
            alphabet.add(tmpIndexItem);
        }
    }

    void setRows(ListView listView,List rows) {
        this.rows = rows;
        alphabetListAdapter.setRows(getListOfContactsAndSections(rows));
        listView.setAdapter(alphabetListAdapter);
        updateSideIndex();
    }

    Pair scrollToSelectedSection(float sideIndexY) {
        int sideIndexHeight = sideIndex.getHeight();
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;
        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);
        // get the item (we can do it since we know item index)
        if (itemPosition < alphabet.size()) {
            TempIndexItem indexItem = alphabet.get(itemPosition);
            if (hasChildes(indexItem)) {
                try {
                    if (!sectionsList.isEmpty()) {
                        SectionIndexedList sectionIndexedList = new SectionIndexedList(indexItem.getLetter());
                        for (int i = ZERO; i < listOfContactsAndSections.size(); i++) {
                            if (listOfContactsAndSections.get(i) instanceof SectionIndexedList) {
                                SectionIndexedList indexedList = (SectionIndexedList) listOfContactsAndSections.get(i);
                                if (sectionIndexedList.getText().equals(indexedList.getText())) {
                                    return new Pair(sectionIndexedList.getText(), i+ ONE);
                                }
                            }
                        }
                    }
                } catch (NullPointerException ignored) {}
            }
        }
        return null;
    }

    private void setSideIndex(){
        int indexListSize = alphabet.size();
        if (indexListSize < ONE) {
            return;
        }
        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / TWENTY);
        int tmpIndexListSize = indexListSize;
        while (tmpIndexListSize > indexMaxSize) {
            tmpIndexListSize = tmpIndexListSize / TWO;
        }
        double delta;
        if (tmpIndexListSize > ZERO) {
            delta = indexListSize / tmpIndexListSize;
        } else {
            delta = ONE;
        }
        TextView tmpTV;
        for (double i = ONE; i <= indexListSize; i = i + delta) {
            TempIndexItem tmpIndexItem = alphabet.get((int) i - ONE);
            String tmpLetter = tmpIndexItem.getLetter();
            tmpTV = new TextView(context);
            tmpTV.setText(tmpLetter);
            tmpTV.setTextSize(setTextScaleInPx(indexedList.getItemsSizeSideIndex()));
            if (hasChildes(tmpIndexItem)) {
                tmpTV.setTextColor(context.getResources().getColor(indexedList.getResColorIdForUnDimmedItems()));
            } else {
                tmpTV.setTextColor(context.getResources().getColor(indexedList.getResColorIdForDimmedItems()));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void updateSideIndex() {
        sideIndex.removeAllViews();
        indexListSize = alphabet.size();
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
        listOfContactsAndSections = new ArrayList();
        final int contactListSize = contactsList.size();
        String firstLetterInSection;
        int index = ZERO;
        for (RowInList section : sectionsList) {
            firstLetterInSection = ((SectionIndexedList) section).getText().substring(ZERO, ONE);
            int occurrence = ZERO;
            listOfContactsAndSections.add(section);
            for (int i = ZERO; i < contactListSize ; i++) {
               try {
                   Contact contact = (Contact) contactsList.get(ZERO);
                   String designerFirstLetter = contact.getName().substring(ZERO, ONE);
                   if (firstLetterInSection.equals(designerFirstLetter)) {
                       ++occurrence;
                       listOfContactsAndSections.add(new CategoryIndexedList(contact.getName(), index));
                       index++;
                       contactsList.remove(ZERO);
                   }
               }catch (RuntimeException e){ // maybe catch that exception many times, depending on the num of contacts in each section
                   Log.e(getClass().getSimpleName(),e.getMessage());
               }
            }
            occurrences.put(firstLetterInSection, occurrence);
            if (occurrence == ZERO) listOfContactsAndSections.remove(section);
        }
        return(listOfContactsAndSections);
    }

    String getTopItemName(ListView listView) throws NullPointerException,ClassCastException{
        View view = listView.getChildAt(ZERO);
        CategoryIndexedList categoryIndexedList = (CategoryIndexedList) view.getTag();
        String name = categoryIndexedList.getCategoryName();
        return name.substring(ZERO, ONE);
    }

    private int setTextScaleInPx(float size) {
        float fpixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size, getDisplayMetrics(context));
        return Math.round(fpixels);
    }

    private DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

}
