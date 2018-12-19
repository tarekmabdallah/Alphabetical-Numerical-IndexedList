package com.gmail.tarekmabdallah91.myindexedlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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

@SuppressWarnings("unchecked")
public class IndexedListFragment extends ListFragment {

    private final static String LATIN_ALPHABET = "#,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
    private final static List<String> LIST_LETTERS = new ArrayList<>(Arrays.asList(LATIN_ALPHABET.split(",")));
    private static final int ZERO = 0;
    private final static String TAG = IndexedListFragment.class.getSimpleName();
    private static List rows;
    private static List<TempIndexItem> alphabet;
    private static List<RowInList> sectionsList;
    private static float sideIndexY;
    private final int NUMBER_OF_ITEMS_IN_SECTION = 10;
    private final HashMap<String, Integer> occurrences = new HashMap<>();
    private List<Contact> contacts;
    private int sideIndexHeight;
    private int indexListSize;
    private TextView sectionStrip;
    private AlphabetListAdapter alphabetListAdapter;
    private LinearLayout sideIndex;
    private Context context;
    private List finalList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_indexed_list, container, false);
        sideIndex = root.findViewById(R.id.sideIndex);
        sectionStrip = root.findViewById(R.id.section_strip);
        context = getContext();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alphabetListAdapter = new AlphabetListAdapter();
        final ListView listView = getListView();
        setListByData();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                try {
                    View view = listView.getChildAt(0);
                    CategoryIndexedList categoryIndexedList = (CategoryIndexedList) view.getTag();
                    String name = categoryIndexedList.getCategoryName();
                    String letter = name.substring(0, 1);
                    sectionStrip.setText(letter);
                } catch (NullPointerException | ClassCastException e) {
                    Log.e(getClass().getSimpleName(), e.getMessage());
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {
            CategoryIndexedList tag = (CategoryIndexedList) v.getTag();
            if (null != tag) {
                Contact currentContact = contacts.get(tag.getIndex());
                Toast.makeText(context, currentContact.getName(), Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void scrollToSelectedSection() {
        sideIndexHeight = sideIndex.getHeight();
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
                        for (int i = 0; i < finalList.size(); i++) {
                            if (finalList.get(i) instanceof SectionIndexedList) {
                                SectionIndexedList indexedList = (SectionIndexedList) finalList.get(i);
                                if (sectionIndexedList.getText().equals(indexedList.getText())) {
                                    getListView().setSelection(i + 1);
                                    sectionStrip.setText(sectionIndexedList.getText());
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                    throw e;
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void updateList() {
        sideIndex.removeAllViews();
        indexListSize = alphabet.size();
        if (indexListSize < 1) {
            return;
        }
        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
        int tmpIndexListSize = indexListSize;
        while (tmpIndexListSize > indexMaxSize) {
            tmpIndexListSize = tmpIndexListSize / 2;
        }
        double delta;
        if (tmpIndexListSize > 0) {
            delta = indexListSize / tmpIndexListSize;
        } else {
            delta = 1;
        }
        TextView tmpTV; // TODO: to edit vertical index here
        for (double i = 1; i <= indexListSize; i = i + delta) {
            TempIndexItem tmpIndexItem = alphabet.get((int) i - 1);
            String tmpLetter = tmpIndexItem.getLetter();
            tmpTV = new TextView(context);
            tmpTV.setText(tmpLetter);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextSize(setTextScaleInPx());
            if (hasChildes(tmpIndexItem)) {
                tmpTV.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                tmpTV.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }
        sideIndexHeight = sideIndex.getHeight();
        sideIndex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // now you know coordinates of touch
                sideIndexY = event.getY();
                // and can display a proper item it country list
                scrollToSelectedSection();
                return false;
            }
        });
    }

    private void setListByData() {
        rows = new ArrayList(); // for sections and items
        alphabet = new ArrayList<>();
        setAlphabeticalList();
        setSectionsList();
    }

    private void setAlphabeticalList() {
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
                end = rows.size() - 1;
                tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK), start, end);
                alphabet.add(tmpIndexItem);
                start = end + 1;
            }
            // Check if we need to add a header row
            if (!firstLetter.equals(previousLetter)) {
                rows.add(new SectionIndexedList(firstLetter));
            }
            previousLetter = firstLetter;
        }
        if (previousLetter != null) {
            // Save the last letter
            tmpIndexItem = new TempIndexItem(previousLetter.toUpperCase(Locale.UK), start, rows.size() - 1);
            alphabet.add(tmpIndexItem);
        }
    }

    private boolean hasChildes(TempIndexItem tempIndexItem) {
        String letter = tempIndexItem.getLetter();
        int occurrence = occurrences.get(letter);
        return occurrence > 0;
    }

    private List<Contact> getDesignersList() {
        List<Contact> contacts = new ArrayList<>();
        List<String> newListLetters = LIST_LETTERS;
        // remove some letters to be dimmed in the sideList
        newListLetters.remove(5);
        newListLetters.remove(8);
        newListLetters.remove(10);
        for (String letter : newListLetters) {
            for (int i = 1; i <= NUMBER_OF_ITEMS_IN_SECTION; i++) {
                contacts.add(new Contact(letter + i));
            }
        }
        return contacts;
    }

    private void setSectionsList() {
        sectionsList = new ArrayList<>();
        finalList = new ArrayList();
        for (String letter : LIST_LETTERS) {
            SectionIndexedList section = new SectionIndexedList(letter);
            sectionsList.add(section);
        }
        List designers = getDesignersList();

        contacts = getDesignersList();
        String firstLetterInSection;
        int index = 0;
        for (RowInList section : sectionsList) {
            firstLetterInSection = ((SectionIndexedList) section).getText().substring(0, 1);
            int occurrence = 0;
            finalList.add(section);
            for (int i = 0; i < designers.size() + NUMBER_OF_ITEMS_IN_SECTION - 1; i++) { // 9 to make the for loop reach to all items in the designers list because in the last item i is bigger than size of list then it breaks the loop
                Contact contact = (Contact) designers.get(ZERO);
                String designerFirstLetter = contact.getName().substring(0, 1);
                if (firstLetterInSection.equals(designerFirstLetter)) {
                    ++occurrence;
                    finalList.add(new CategoryIndexedList(contact.getName(), index));
                    index++;
                    designers.remove(ZERO);
                }
            }
            occurrences.put(firstLetterInSection, occurrence);
            if (occurrence == 0) finalList.remove(section);
        }
        alphabetListAdapter.setRows(finalList);
        setListAdapter(alphabetListAdapter);
        updateList();
    }

    private int setTextScaleInPx() {
        float fpixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (float) 12.0, getDisplayMetrics());
        return Math.round(fpixels);
    }

    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }
}
