package com.example.assignment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Expandable extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private List<String> groupData;
    private HashMap<String, List<String>> childData;
    private int lastExpandedGroup = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expandable);

        // Apply edge-to-edge insets for proper layout adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views and data
        expandableListView = findViewById(R.id.expandableListView);
        initializeData();

        // Set adapter
        ExpandableListAdapter adapter = new com.example.assignment.adapter.ExpandableListAdapter(this, groupData, childData);

        expandableListView.setAdapter(adapter);

        // Group click listener
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            String groupText = groupData.get(groupPosition);
            Toast.makeText(getApplicationContext(), "Group: " + groupText, Toast.LENGTH_SHORT).show();
            return false;
        });

        // Group collapse listener
        expandableListView.setOnGroupCollapseListener(groupPosition -> {
            String groupText = groupData.get(groupPosition);
            Toast.makeText(getApplicationContext(), groupText + " collapsed", Toast.LENGTH_SHORT).show();
        });

        // Child click listener
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String childText = childData.get(groupData.get(groupPosition)).get(childPosition);
            Toast.makeText(getApplicationContext(), "Selected: " + childText, Toast.LENGTH_SHORT).show();
            return false;
        });

        // Group expand listener with auto-collapse for other groups
        expandableListView.setOnGroupExpandListener(groupPosition -> {
            if (lastExpandedGroup != -1 && lastExpandedGroup != groupPosition) {
                expandableListView.collapseGroup(lastExpandedGroup);
            }
            lastExpandedGroup = groupPosition;
        });
    }

    // Method to initialize group and child data
    private void initializeData() {
        String[] headers = getResources().getStringArray(R.array.fruit_groups);
        String[][] children = {
                getResources().getStringArray(R.array.citrus_fruits),
                getResources().getStringArray(R.array.berry_fruits),
                getResources().getStringArray(R.array.tropical_fruits)
        };

        groupData = new ArrayList<>();
        childData = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            groupData.add(headers[i]);
            List<String> childList = new ArrayList<>();
            for (String child : children[i]) {
                childList.add(child);
            }
            childData.put(headers[i], childList);
        }
    }
}
