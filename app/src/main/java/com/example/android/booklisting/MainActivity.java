package com.example.android.booklisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    public EditText mEditText;
    public String user_query;
    public Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText=(EditText)findViewById(R.id.main_edit_text);
        mSearchButton=(Button)findViewById(R.id.user_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_query=mEditText.getText().toString();
                Intent intent = new Intent(MainActivity.this,BooklistView.class);
                intent.putExtra("location",user_query);
                startActivity(intent);
            }
        });
    }
}
