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

package com.gmail.tarekmabdallah91.indexedlistview.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemIndexedList extends RowInList implements Parcelable {

    private String name;
    private String parent;
    private int index;

    protected ItemIndexedList() {
    }

    public ItemIndexedList(String name) {
        this.name = name;
    }

    public static final Creator<ItemIndexedList> CREATOR = new Creator<ItemIndexedList>() {
        @Override
        public ItemIndexedList createFromParcel(Parcel in) {
            return new ItemIndexedList(in);
        }

        @Override
        public ItemIndexedList[] newArray(int size) {
            return new ItemIndexedList[size];
        }
    };

    private ItemIndexedList(Parcel in) {
        name = in.readString();
        parent = in.readString();
        index = in.readInt();
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public String getParent() {
        return parent;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String categoryName) {
        this.name = categoryName;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(parent);
        dest.writeInt(index);
    }
}
