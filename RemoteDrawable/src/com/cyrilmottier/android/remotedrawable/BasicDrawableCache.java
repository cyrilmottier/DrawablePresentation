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

package com.cyrilmottier.android.remotedrawable;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.cyrilmottier.android.remotedrawable.util.Config;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * This is a very basic implementation of a cache drawable.
 * 
 * @author Cyril Mottier
 */
public class BasicDrawableCache {

    // TODO cyril: This is the most simple cache implementation ever. A better
    // implementation would have been to take into account the lifetime of all
    // objects included in that cache. Implementing such a system would have
    // been great to force some Drawable to refresh. Another feature would have
    // been to use the SD card for "most important" Drawables.

    private static final boolean DEBUG_LOGS_FILE_ENABLED = true;
    private static final boolean DEBUG_LOGS_ENABLED = DEBUG_LOGS_FILE_ENABLED && Config.DEBUG_LOGS_PROJECT_ENABLED;
    private static final String LOG_TAG = BasicDrawableCache.class.getSimpleName();

    private HashMap<String, SoftReference<Drawable>> mCache;

    public BasicDrawableCache() {
        mCache = new HashMap<String, SoftReference<Drawable>>();
    }

    public Drawable get(String key) {
        final SoftReference<Drawable> softRef = mCache.get(key);
        return (softRef == null) ? null : softRef.get();
    }

    public void put(String key, Drawable element) {
        if (DEBUG_LOGS_ENABLED) {
            Log.d(LOG_TAG, "Inserting/replacing drawable from URL:" + key);
        }
        mCache.put(key, new SoftReference<Drawable>(element));
    }

}
