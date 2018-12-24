
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

package com.gmail.tarekmabdallah91.myindexedlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel.IndexedList;
import com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel.IndexedListListener;
import com.gmail.tarekmabdallah91.indexedlistview.models.ItemIndexedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String LATIN_ALPHABET = "A,B,C,D,E,F,G,H,I";
    private final static String NUMERICAL_LIST = "1,2,3,4,5,6,7,8,9";
    private  List<String> LIST_LETTERS = new ArrayList<>(Arrays.asList(LATIN_ALPHABET.split(",")));
    private  List<String> LIST_NUMBERS = new ArrayList<>(Arrays.asList(NUMERICAL_LIST.split(",")));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndexedList.getInstance(this)
                .setResFrameLayout(R.id.list_fragment)
                .setNumericalList(NUMERICAL_LIST)
                .seItemsList(getDaysList())
             //   .setAlphabeticalList(LATIN_ALPHABET)
               // .seItemsList(getDesignersList())
                .setDimmedColorInSideIndex(android.R.color.holo_red_dark)
                .setItemsSizeSideIndex(10f)
                .setItemsTextSize(12f)
                .setSectionTextSize(16f)
                .setStripTextSize(16f)
                .setResColorStrip(R.color.colorPrimaryDark)
                .setResColorSection(R.color.colorAccent)
                .setResBackgroundColorIdStrip(android.R.color.holo_green_light)
                .setIndexedListListener(new IndexedListListener() {
                    @Override
                    public void onClickListItem(View v, int position, long id) {
                        try {
                            ItemIndexedList tag = (ItemIndexedList) v.getTag();
                            if (null != tag) {
                                Toast.makeText(getBaseContext(), tag.getName(), Toast.LENGTH_LONG).show();
                            }
                        } catch (NumberFormatException | IndexOutOfBoundsException ignored) {}
                    }
                })
                .start();
    }

    private List<ItemIndexedList> getDesignersList() {
        List<ItemIndexedList> contacts = new ArrayList<>();
        List<String> newListLetters = LIST_LETTERS;
        // remove some letters to be dimmed in the sideList
        newListLetters.remove("E");
        newListLetters.remove("G");
        newListLetters.remove("V");
        for (String letter : newListLetters) {
            int NUMBER_OF_ITEMS_IN_SECTION = 10;
            for (int i = 1; i <= NUMBER_OF_ITEMS_IN_SECTION; i++) {
                contacts.add(new ItemIndexedList(letter + i));
            }
        }
        contacts.add(new ItemIndexedList("@@@"));
        contacts.add(new ItemIndexedList("55555"));
        return contacts;
    }

    private List<ItemIndexedList> getDaysList() {
        List<ItemIndexedList> days = new ArrayList<>();
        List<String> newListLetters = LIST_NUMBERS;
        // remove some letters to be dimmed in the sideList
        for (String letter : newListLetters) {
            int NUMBER_OF_ITEMS_IN_SECTION = 10;
            for (int i = 1; i <= NUMBER_OF_ITEMS_IN_SECTION; i++) {
                days.add(new ItemIndexedList(letter + i));
            }
        }
        return days;
    }
}
