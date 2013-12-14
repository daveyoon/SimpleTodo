package com.ylab.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditTodo extends Activity {
	private EditText etEditItem;
	private int pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_todo);		
		String todo = getIntent().getStringExtra("todo");		
		pos = getIntent().getIntExtra("pos",0);
		etEditItem = (EditText) findViewById(R.id.etEditTodoItem);
		etEditItem.setText(todo);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_todo, menu);
		return true;
	}
	
	
	public void saveTodoItem(View v) {
		Intent data = new Intent();		
		data.putExtra("editedTodo", etEditItem.getText().toString());
		data.putExtra("pos",pos);		
		setResult(RESULT_OK, data);		
		finish();
	}

}
