package com.example.assignment;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button btn;


    String[] items = {"Apple", "Banana", "Grape","Mango","Pineapple"};
    Integer[] imgid = {R.drawable.apple, R.drawable.banana, R.drawable.grape,R.drawable.mango,R.drawable.pineapple};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn= findViewById(R.id.btn_exp);

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Expandable.class);
            startActivity(intent);
        });


        listView = findViewById(R.id.simpleListView);


        CustomAdapter adapter = new CustomAdapter();
        listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items[position];
            Toast.makeText(MainActivity.this, "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
    }


    private class CustomAdapter extends ArrayAdapter<String> {

        public CustomAdapter() {
            super(MainActivity.this, R.layout.list_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);

                holder = new ViewHolder();
                holder.textView = convertView.findViewById(R.id.textview);
                holder.imageView = convertView.findViewById(R.id.imageView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.textView.setText(items[position]);
            holder.imageView.setImageResource(imgid[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView textView;
            ImageView imageView;

        }

    }

}
