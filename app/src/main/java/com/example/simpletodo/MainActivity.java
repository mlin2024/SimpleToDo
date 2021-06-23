package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button addButton;
    EditText editTextItem;
    RecyclerView recyclerViewItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        editTextItem = findViewById(R.id.editTextItem);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        items = new ArrayList<>();
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item that was long pressed
                items.remove(position);

                // Notify the adapter that the item has been deleted
                itemsAdapter.notifyItemRemoved(position);

                // Let the user know it worked with a toast
                Toast.makeText(getApplicationContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        recyclerViewItems.setAdapter(itemsAdapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab string from the edit text item
                String todoItem = editTextItem.getText().toString();

                // Only add it if there was actually something in the edit text item
                if (!todoItem.equals("")) {
                    // Add item to model
                    items.add(todoItem);

                    // Notify the adapter that the item has been added
                    itemsAdapter.notifyItemInserted(items.size() - 1);

                    // Clear the edit text item
                    editTextItem.setText("");

                    // Let the user know it worked with a toast
                    Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Let the user know it didn't work with a toast
                    Toast.makeText(getApplicationContext(), "Todo item is blank", Toast.LENGTH_SHORT).show();
                }

                saveItems();
            }
        });
    }

    // Gets file in the current directory called data.txt
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // Parses through the file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // Writes to the file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving items", e);
        }
    }
}