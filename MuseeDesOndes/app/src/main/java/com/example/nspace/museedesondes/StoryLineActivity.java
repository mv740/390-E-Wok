package com.example.nspace.museedesondes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StoryLineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_line);

        final Button exploration_button = (Button) findViewById(R.id.exploration_button);
        exploration_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent startMap = new Intent(StoryLineActivity.this, MapActivity.class);
                        startActivity(startMap);
                    }
                }
        );
    }
}
