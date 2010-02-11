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

package com.cyrilmottier.android.flattenhierarchy;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

/**
 * The following activity shows how to create an optimized splash screen that
 * adapts to different screen resolution and densities using as few Views as
 * possible. The trick is based on some Drawables' features.
 * 
 * @author Cyril Mottier
 */
public class MainActivity extends Activity {

    private static final int REGULAR_SPLASH_SCREEN = 0;
    private static final int OPTIMIZED_SPLASH_SCREEN = 1;
    private static final int VERY_OPTIMIZED_SPLASH_SCREEN = 2;

    /**
     * Set the following variable to switch between the "regular", "optimized"
     * and "very optimized" methods.
     */
    private static final int CURRENT_SPLASH_SCREEN = REGULAR_SPLASH_SCREEN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (CURRENT_SPLASH_SCREEN) {
            case REGULAR_SPLASH_SCREEN:

                /*
                 * The first method is considered as the "regular" way to create
                 * a splash screen. It consists on creating a simple FrameLayout
                 * used to layout all sub-ImageViews. Doing so, the FrameLayout
                 * takes care of replacing elements at the correct position when
                 * the screen size changes (during orientation changes for
                 * instance). See resources/regular.png to see the view
                 * hierarchy.
                 */
                setContentView(R.layout.regular_splash_screen);
                break;

            case OPTIMIZED_SPLASH_SCREEN:

                /*
                 * This is the cleanest and most optimized way to create your
                 * splash screen. It uses a single view displaying all splash
                 * screen components via a LayerDrawable. See
                 * resources/optimized.png to see the view hierarchy.
                 */
                setContentView(R.layout.optimized_splash_screen);
                break;

            case VERY_OPTIMIZED_SPLASH_SCREEN:

                /*
                 * This is the most optimized way to create a splash screen. The
                 * following splash screen is displayed by creating no Views at
                 * all! It is simply composed of several Drawables. (see XML
                 * files). This snippet of code is presented here to show you
                 * how a view hierarchy can be flatten to nothing. However using
                 * this "hack" is VERY DISCOURAGED as it uses a hard-coded
                 * dimension (status_bar_height) that may differ between Android
                 * builds. See resources/very_optimized.png to see the view
                 * hierarchy.
                 */
                Drawable d = getResources().getDrawable(R.drawable.ss_background);
                findViewById(android.R.id.content).setBackgroundDrawable(d);
                break;
                
        }
    }
}
