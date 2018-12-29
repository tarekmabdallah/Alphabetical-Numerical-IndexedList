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
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import static com.gmail.tarekmabdallah91.indexedlistview.IndexedListModel.IndexedListPresenter.ZERO;

public final class IndexedList {

    private String LIST_INDICES = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
    private boolean isAlphabetical = true;
    private AppCompatActivity activity;
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

    private IndexedList(AppCompatActivity activity){
        this.activity = activity;
    }

    public static IndexedList getInstance(AppCompatActivity activity){
        if (null == indexedList){
            synchronized (IndexedList.class) {
                indexedList = new IndexedList(activity);
            }
        }
        return indexedList;
    }

    public void start(){
        setIndexedListFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(resFrameLayout,indexedListFragment)
                .commit();
    }

    public void notifyDataChanges(){
        setIndexedListFragment();
        start();
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

    float getStripTextSize() {
        return stripTextSize;
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

    public IndexedList setAlphabeticalList(String alphabeticalList) {
        LIST_INDICES = alphabeticalList;
        return this;
    }

    public IndexedList setNumericalList(String numericalList) {
        LIST_INDICES = numericalList;
        isAlphabetical = false;
        return this;
    }

    String getLIST_INDICES() {
        return LIST_INDICES;
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
    public void onDestroy (){
        try {
            indexedList.finalize();
            indexedList = null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
