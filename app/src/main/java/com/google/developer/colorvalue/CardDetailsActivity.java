package com.google.developer.colorvalue;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.developer.colorvalue.data.CardAdapter;
import com.google.developer.colorvalue.data.CardProvider;
import com.google.developer.colorvalue.service.CardService;
import com.google.developer.colorvalue.ui.ColorView;

public class CardDetailsActivity extends AppCompatActivity {
    private TextView color_nameTextView;
    private TextView color_hexTextView;
    private ColorView colorView;
    private int  color_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        color_id = getIntent().getIntExtra("color_id",-1);

        color_hexTextView = (TextView) findViewById(R.id.hex_tv);
        color_nameTextView = (TextView) findViewById(R.id.color_name_tv);
        colorView = (ColorView) findViewById(R.id.color_view);

        if (colorView.isShowingText()){
            colorView.hideText();
        }

        if (getIntent().getStringExtra(CardAdapter.STRING_COLOR_HEX) != null){
            colorView.setBackgroundColor(Color.parseColor(getIntent().getStringExtra(CardAdapter.STRING_COLOR_HEX)));
            color_hexTextView.setText(getIntent().getStringExtra(CardAdapter.STRING_COLOR_HEX));
        }
/*        if (getIntent().getStringExtra("color_background") != null) {
            Toast.makeText(this,"Color : "+getIntent().getStringExtra("color_background"),Toast.LENGTH_LONG).show();
            colorView.setBackgroundColor(Integer.parseInt(getIntent().getStringExtra("color_background")));
        }*/
        if (getIntent().getStringExtra(CardAdapter.STRING_COLOR_NAME) != null){
            color_nameTextView.setText(getIntent().getStringExtra(CardAdapter.STRING_COLOR_NAME));
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("ColorValue");
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        } else if (id == R.id.action_delete){
        String stringId = Integer.toString(color_id);
            Uri uri = CardProvider.Contract.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            CardService.deleteCard(this,uri);
            getContentResolver().delete(uri,null,null);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
