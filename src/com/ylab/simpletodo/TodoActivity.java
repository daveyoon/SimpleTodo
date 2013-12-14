package com.ylab.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {

	private ArrayList<String> todoItems; //Array of strings to store the values of the todo
	private ArrayAdapter<String> todoAdapter; //Adapter to bind the array to the ListView
	private ListView lvItems; //UI Element for the list view
	private EditText etNewItem; //UI Element for the new todo item textfield
	
	//Constants for Intent Views
	private final int EDIT_TODO_CODE = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        readItems(); //populate the todoItems array from the textfile.
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        etNewItem = (EditText) findViewById(R.id.etAddItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener(); //attach click events to the view (long click for delete, single click for edit) 
    }
    
    private void setupListViewListener() { 
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){
    		@Override
    		public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
    			//remote the item given the position
    			todoItems.remove(pos);
    			//notify the adapter
    			todoAdapter.notifyDataSetChanged();
    			writeItems();
    			return true; 
    		}
    	});
    	//Add an intent to open up an edit activity whenever an item is clicked.
    	lvItems.setOnItemClickListener(new OnItemClickListener(){
    		public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
    			Intent i = new Intent(TodoActivity.this, EditTodo.class);
    			i.putExtra("todo", todoItems.get(pos)); //add the text of the current item
    			i.putExtra("pos", pos); //add the position
    			startActivityForResult(i,EDIT_TODO_CODE);
    		}
    	});
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }
    
    public void addTodoItem(View v) {    	
    	String itemText = etNewItem.getText().toString();
    	todoAdapter.add(itemText);
    	etNewItem.setText("");
    	writeItems();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //Code to update the todo list and save the edited item to the write file
      if (resultCode == RESULT_OK && requestCode == EDIT_TODO_CODE) {
         String todoValue = data.getExtras().getString("editedTodo");
         int pos = data.getExtras().getInt("pos");
         todoItems.set(pos, todoValue);
         todoAdapter.notifyDataSetChanged();         
         writeItems();
      }
    } 
    
    private void readItems() { 
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try { 
    		todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e){
    		todoItems = new ArrayList<String>();
    	}
    }
    
    private void writeItems() { 
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try { 
    		FileUtils.writeLines(todoFile, todoItems);    	
    	} catch(IOException e) { 
    		e.printStackTrace();
    	}
    }
}
