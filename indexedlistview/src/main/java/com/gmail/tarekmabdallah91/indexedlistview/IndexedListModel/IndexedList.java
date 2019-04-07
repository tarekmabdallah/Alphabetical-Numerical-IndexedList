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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import static com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel.IndexedListPresenter.ZERO;

@SuppressWarnings("FinalizeCalledExplicitly")
public final class IndexedList implements Parcelable {
    private String LIST_INDICES = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
    private boolean isAlphabetical = true;
    public static final Creator<IndexedList> CREATOR = new Creator<IndexedList>() {
        @Override
        public IndexedList createFromParcel(Parcel in) {
            return new IndexedList(in);
        }

        @Override
        public IndexedList[] newArray(int size) {
            return new IndexedList[size];
        }
    };
    private int resFrameLayout;
    private float stripTextSize = 18f;
    private float sectionTextSize = 18f;
    private float itemsTextSize = 14f;
    private float itemsSizeSideIndex = 10f;
    private int paddingLeft = ZERO;
    private int paddingRight = ZERO;
    private int paddingTop = ZERO;
    private int paddingBottom = ZERO;
    private int resColorItem = android.R.color.darker_gray;
    private int resColorSection = android.R.color.black;
    private int resColorStrip = android.R.color.black;
    private int resColorIdForUnDimmedItems = android.R.color.black;
    private int resColorIdForDimmedItems = android.R.color.darker_gray;
    private int resBackgroundColorIdStrip = android.R.color.transparent;
    private IndexedListFragment indexedListFragment;
    private List items;
    private IndexedListListener indexedListListener;
    private IndexedListAdapter indexedListAdapter;
    private int resBackgroundColor = android.R.color.white;
    @SuppressLint("StaticFieldLeak")
    private static volatile IndexedList indexedList = null;

    private IndexedList(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    private IndexedList(Parcel in) {
        LIST_INDICES = in.readString();
        isAlphabetical = in.readByte() != 0;
        resFrameLayout = in.readInt();
        stripTextSize = in.readFloat();
        sectionTextSize = in.readFloat();
        itemsTextSize = in.readFloat();
        itemsSizeSideIndex = in.readFloat();
        paddingLeft = in.readInt();
        paddingRight = in.readInt();
        paddingTop = in.readInt();
        paddingBottom = in.readInt();
        resColorItem = in.readInt();
        resColorSection = in.readInt();
        resColorStrip = in.readInt();
        resColorIdForUnDimmedItems = in.readInt();
        resColorIdForDimmedItems = in.readInt();
        resBackgroundColorIdStrip = in.readInt();
        resBackgroundColor = in.readInt();
    }

    public static IndexedList getInstance(AppCompatActivity activity) {
        if (null == indexedList) {
            synchronized (IndexedList.class) {
                indexedList = new IndexedList(activity);
            }
        }
        return indexedList;
    }

    public void start() {
        setIndexedListFragment();
        try {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(resFrameLayout, indexedListFragment)
                    .commit();
        } catch (IllegalStateException ignored) {
        }
    }

    List getItems() {
        return items;
    }

    IndexedListListener getIndexedListListener() {
        return indexedListListener;
    }

    int getResBackgroundColor() {
        return resBackgroundColor;
    }

    AppCompatActivity getActivity() {
        return activity;
    }

    float getSectionTextSize() {
        return sectionTextSize;
    }

    float getItemsTextSize() {
        return itemsTextSize;
    }

    int getResColorItem() {
        return resColorItem;
    }

    int getResColorSection() {
        return resColorSection;
    }

    float getItemsSizeSideIndex() {
        return itemsSizeSideIndex;
    }

    int getResColorIdForUnDimmedItems() {
        return resColorIdForUnDimmedItems;
    }

    int getResColorIdForDimmedItems() {
        return resColorIdForDimmedItems;
    }

    int getPaddingLeft() {
        return paddingLeft;
    }

    int getPaddingRight() {
        return paddingRight;
    }

    int getPaddingTop() {
        return paddingTop;
    }

    int getPaddingBottom() {
        return paddingBottom;
    }

    int getResBackgroundColorIdStrip() {
        return resBackgroundColorIdStrip;
    }

    int getResColorStrip() {
        return resColorStrip;
    }

    IndexedListAdapter getIndexedListAdapter() {
        return indexedListAdapter;
    }

    public IndexedList setIndexedListAdapter(IndexedListAdapter indexedListAdapter) {
        this.indexedListAdapter = indexedListAdapter;
        return this;
    }

    boolean isAlphabetical() {
        return isAlphabetical;
    }

    public IndexedList setNumericalList(String numericalList) {
        LIST_INDICES = numericalList;
        isAlphabetical = false;
        return this;
    }

    String getLIST_INDICES() {
        return LIST_INDICES;
    }

    private void setIndexedListFragment() {
        indexedListFragment = new IndexedListFragment();
        indexedListFragment.setIndexedList(this);
    }

    public IndexedList seItemsList(List items) {
        this.items = items;
        return this;
    }

    public IndexedList setResFrameLayout(int resFrameLayout) {
        this.resFrameLayout = resFrameLayout;
        return this;
    }

    public IndexedList setResBackgroundColorIdStrip(int resBackgroundColorIdStrip) {
        this.resBackgroundColorIdStrip = resBackgroundColorIdStrip;
        return this;
    }

    public IndexedList setResColorStrip(int resColorStrip) {
        this.resColorStrip = resColorStrip;
        return this;
    }

    /**
     * must be called in onStop to avoid IllegalStateException
     */
    public void onDestroy() {
        try {
            indexedList.finalize();
            indexedList = null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LIST_INDICES);
        dest.writeByte((byte) (isAlphabetical ? 1 : 0));
        dest.writeInt(resFrameLayout);
        dest.writeFloat(stripTextSize);
        dest.writeFloat(sectionTextSize);
        dest.writeFloat(itemsTextSize);
        dest.writeFloat(itemsSizeSideIndex);
        dest.writeInt(paddingLeft);
        dest.writeInt(paddingRight);
        dest.writeInt(paddingTop);
        dest.writeInt(paddingBottom);
        dest.writeInt(resColorItem);
        dest.writeInt(resColorSection);
        dest.writeInt(resColorStrip);
        dest.writeInt(resColorIdForUnDimmedItems);
        dest.writeInt(resColorIdForDimmedItems);
        dest.writeInt(resBackgroundColorIdStrip);
        dest.writeInt(resBackgroundColor);
    }

    public void notifyDataChanges() {
        setIndexedListFragment();
        start();
    }

    float getStripTextSize() {
        return stripTextSize;
    }

    public IndexedList setAlphabeticalList(String alphabeticalList) {
        LIST_INDICES = alphabeticalList;
        return this;
    }


    public IndexedList setDimmedColorInSideIndex(int resColorIdForDimmedItems){
        this.resColorIdForDimmedItems = resColorIdForDimmedItems;
        return this;
    }

    public IndexedList setResColorIdForUnDimmedItems(int resColorIdForUnDimmedItems) {
        this.resColorIdForUnDimmedItems = resColorIdForUnDimmedItems;
        return this;
    }

    public IndexedList setItemsSizeSideIndex(float itemsSizeSideIndex) {
        this.itemsSizeSideIndex = itemsSizeSideIndex;
        return this;
    }

    public IndexedList setItemsTextSize(float itemsTextSize) {
        this.itemsTextSize = itemsTextSize;
        return this;
    }

    public IndexedList setSectionTextSize(float sectionTextSize) {
        this.sectionTextSize = sectionTextSize;
        return this;
    }

    public IndexedList setResColorItem(int resColorItem) {
        this.resColorItem = resColorItem;
        return this;
    }

    public IndexedList setResColorSection(int resColorSection) {
        this.resColorSection = resColorSection;
        return this;
    }

    public IndexedList setResBackgroundColor(int resBackgroundColor) {
        this.resBackgroundColor = resBackgroundColor;
        return this;
    }

    public IndexedList setIndexedListListener(IndexedListListener indexedListListener) {
        this.indexedListListener = indexedListListener;
        return this;
    }

    public IndexedList setPadding(int paddingLeft, int paddingTop ,int paddingRight, int paddingBottom) {
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
        return this;
    }

    public IndexedList setStripTextSize(float stripTextSize) {
        this.stripTextSize = stripTextSize;
        return this;
    }

    public IndexedList setResColorIdForDimmedItems(int resColorIdForDimmedItems) {
        this.resColorIdForDimmedItems = resColorIdForDimmedItems;
        return this;
    }
}
