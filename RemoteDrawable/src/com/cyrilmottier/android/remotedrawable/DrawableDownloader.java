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

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

import com.cyrilmottier.android.remotedrawable.util.Config;

/**
 * The DrawableDownloader takes care of downloading Drawable according to a
 * given URL. It includes a cache that prevent downloading previously downloaded
 * Drawables.
 * 
 * @author Cyril Mottier
 */
public class DrawableDownloader {

    static interface Callback {
        void onDrawableLoaded(String urlString, Drawable drawable);

        void onDrawableLoadingFailed(String urlString);
    }

    private static final boolean DEBUG_LOGS_FILE_ENABLED = true;
    private static final boolean DEBUG_LOGS_ENABLED = DEBUG_LOGS_FILE_ENABLED && Config.DEBUG_LOGS_PROJECT_ENABLED;
    private static final String LOG_TAG = DrawableDownloader.class.getSimpleName();

    private static final int POOL_THREAD_NUMBER = 3;
    private static DrawableDownloader sInstance;

    private final ExecutorService mPool;
    private final BasicDrawableCache mCache;
    private final Handler mHandler = new Handler();

    private DrawableDownloader() {
        mPool = Executors.newFixedThreadPool(POOL_THREAD_NUMBER);
        mCache = new BasicDrawableCache();
    }

    public static DrawableDownloader getInstance() {
        if (sInstance == null) {
            sInstance = new DrawableDownloader();
        }
        return sInstance;
    }

    public Drawable getDrawable(final String urlString, final Callback callback) {
        final Drawable d = mCache.get(urlString);
        if (d != null) {
            return d;
        }

        // TODO cyril: The following code has been written for a demo purpose
        // only. It doesn't work properly when device orientation is changing
        // for instance. Here, we should first be sure, the urlString is not a
        // pending or running task to prevent downloading useless drawables.
        // Unfortunatly, doing so implies using or own thread pool (in which
        // each queued task is known) - which is not the purpose of this
        // demonstration.

        // TODO cyril: Nothing is done to handle timeouts or network
        // connectivity problem ... Is the "catch" bloc a sufficient code for
        // that?

        mPool.execute(new Runnable() {

            public void run() {
                try {

                    if (DEBUG_LOGS_ENABLED) {
                        Log.d(LOG_TAG, "Starting to download at " + urlString);
                    }

                    URL url = new URL(urlString);
                    InputStream is = (InputStream) url.getContent();
                    final Drawable drawable = Drawable.createFromStream(is, null);
                    mCache.put(urlString, drawable);

                    if (DEBUG_LOGS_ENABLED) {
                        Log.d(LOG_TAG, "Download completed at " + urlString);
                    }

                    mHandler.post(new Runnable() {
                        public void run() {
                            // TODO cyril: Actually we should call the callback
                            // here on ALL RemoteDrawable that asked for the
                            // given url. (In case two different Drawable asked
                            // for the same URL and a pending/running URL hasn't
                            // been re-downloaded - cf previous TODO)
                            callback.onDrawableLoaded(urlString, drawable);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {

                        public void run() {
                            callback.onDrawableLoadingFailed(urlString);
                        }
                    });
                }
            }
        });

        // Here null is returned to indicate the Drawable is not ready
        return null;
    }
}
