package io.griesser.simpletodo.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.griesser.simpletodo.R;
import io.griesser.simpletodo.activities.EditItemActivity;
import io.griesser.simpletodo.activities.MainActivity;
import io.griesser.simpletodo.models.TodoItem;

import java.util.ArrayList;

import static io.griesser.simpletodo.R.id.lvItems;
import static io.griesser.simpletodo.activities.MainActivity.EXTRA_TODOITEM;

public class SortByDialogFragment extends DialogFragment {

    public interface SortByDialogListener {
        void onSortBySelected(String sortBy);
    }


    private final static String ARG_SORTING = "sorting";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listViewSortBy;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SortByDialogFragment() {
    }

    public static SortByDialogFragment newInstance(String currentSorting) {
        SortByDialogFragment fragment = new SortByDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SORTING, currentSorting);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort by");

        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SortByDialogListener listener = (SortByDialogListener) getActivity();
                listener.onSortBySelected(items.get(listViewSortBy.getCheckedItemPosition()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_sortby, null);

        String currentSorting = getArguments().getString(ARG_SORTING);

        // Get field from view
        listViewSortBy = (ListView) view.findViewById(R.id.listViewSortBy);

        items = new ArrayList<String>();
        items.add(MainActivity.SORT_BY_NAME_ASC);
        items.add(MainActivity.SORT_BY_NAME_DESC);
        items.add(MainActivity.SORT_BY_DATE_ASC);
        items.add(MainActivity.SORT_BY_DATE_DESC);
        items.add(MainActivity.SORT_BY_PRIORITY_ASC);
        items.add(MainActivity.SORT_BY_PRIORITY_DESC);

        itemsAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, items);
        listViewSortBy.setAdapter(itemsAdapter);

        if(items.contains(currentSorting))
            listViewSortBy.setItemChecked(items.indexOf(currentSorting), true);

        builder.setView(view);
        return builder.create();
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sortby, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle("Sort by");
        String currentSorting = getArguments().getString(ARG_SORTING);

        // Get field from view
        listViewSortBy = (ListView) view.findViewById(R.id.listViewSortBy);

        items = new ArrayList<String>();
        items.add(MainActivity.SORT_BY_NAME_ASC);
        items.add(MainActivity.SORT_BY_NAME_DESC);
        items.add(MainActivity.SORT_BY_DATE_ASC);
        items.add(MainActivity.SORT_BY_DATE_DESC);
        items.add(MainActivity.SORT_BY_PRIORITY_ASC);
        items.add(MainActivity.SORT_BY_PRIORITY_DESC);

        itemsAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, items);
        listViewSortBy.setAdapter(itemsAdapter);

        listViewSortBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SortByDialogListener listener = (SortByDialogListener) getActivity();
                listener.onSortBySelected(items.get(i));
                getDialog().dismiss();
            }
        });

    }

}
