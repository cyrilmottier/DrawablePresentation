/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyrilmottier.android.drawablelisting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*
         * Let's retrieve the list using its ID
         */
        final ListView listView = (ListView) findViewById(R.id.listView);
        final String[] drawableNames = getResources().getStringArray(R.array.drawables_name);

        /*
         * A new adapter is created and set to the previous ListView. An
         * OnItemClickListener is also set in order to start a new activity
         * depending on the index of the clicked row.
         */
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawableNames));
        listView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DisplayDrawableActivity.class);
                intent.putExtra(DisplayDrawableActivity.INDEX_INTENT_KEY, position);
                startActivity(intent);
            }

        });
    }

}
