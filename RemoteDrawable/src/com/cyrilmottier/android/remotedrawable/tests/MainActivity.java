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

package com.cyrilmottier.android.remotedrawable.tests;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;

import com.cyrilmottier.android.remotedrawable.R;
import com.cyrilmottier.android.remotedrawable.RemoteDrawable;

public class MainActivity extends Activity {

    private static final String PHOTO_URL_1 = "http://lh4.ggpht.com/_OHO4y8YcQbs/So-RqqF20zI/AAAAAAAALBo/E3AyJZH8dQM/s800/P1090212.JPG";
    private static final String PHOTO_URL_2 = "http://lh4.ggpht.com/_OHO4y8YcQbs/So4bVoPnrnI/AAAAAAAAKu4/YKpcgLiX--g/s800/P1080885.JPG";
    private static final String PHOTO_URL_3 = "http://lh4.ggpht.com/_OHO4y8YcQbs/SpDMgpLlfKI/AAAAAAAALLE/SixfPkpLB40/s800/P1090391.JPG";
    private static final String PHOTO_URL_4 = "http://lh3.ggpht.com/_OHO4y8YcQbs/SoWDYIhFrjI/AAAAAAAAKX4/ETS4JGuUYX0/s800/P1080412.JPG";

    private static final int TRANSITION_DURATION = 4000;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TransitionDrawable transition = (TransitionDrawable) getResources().getDrawable(R.drawable.transition);

        View v = findViewById(R.id.textView1);
        Drawable d = new RemoteDrawable(transition, PHOTO_URL_1);
        v.setBackgroundDrawable(d);
        transition.startTransition(TRANSITION_DURATION); 

        v = findViewById(R.id.textView2);
        d = new RemoteDrawable(new ColorDrawable(Color.RED), PHOTO_URL_2);
        v.setBackgroundDrawable(d);

        v = findViewById(R.id.textView3);
        d = new RemoteDrawable(new ColorDrawable(Color.RED), PHOTO_URL_3);
        v.setBackgroundDrawable(d);

        v = findViewById(R.id.textView4);
        d = new RemoteDrawable(new ColorDrawable(Color.RED), PHOTO_URL_4);
        v.setBackgroundDrawable(d);

    }
}
