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

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.cyrilmottier.android.remotedrawable.util.Config;

/**
 * A RemoteDrawable is a Drawable that is composed of two other Drawables. The
 * first one is a placeholder and the second one is a Drawable that is
 * downloaded asyncronously on the network. The placeholder is display as long
 * as the distant Drawable is not ready. Once the distant Drawable is ready, it
 * replaces the placeholder.
 * 
 * @author cyrilmottier
 */
public class RemoteDrawable extends Drawable implements DrawableDownloader.Callback, Drawable.Callback {

    /*
     * TODO cyril: This sample code as been created quite rapidly to show
     * developers how to create a Drawable. It hasn't been tested at all and
     * therefore many bugs and not-implemented methods may exists. For instance
     * it's not working properly during orientation changes.
     */

    private static final boolean DEBUG_LOGS_FILE_ENABLED = true;
    private static final boolean DEBUG_LOGS_ENABLED = DEBUG_LOGS_FILE_ENABLED && Config.DEBUG_LOGS_PROJECT_ENABLED;
    private static final String LOG_TAG = RemoteDrawable.class.getSimpleName();

    private RemoteState mRemoteState;
    private Drawable mCurrentDrawable;
    private boolean mMutated;

    public RemoteDrawable(Drawable placeholder, String URL) {
        Drawable d = DrawableDownloader.getInstance().getDrawable(URL, this);
        mCurrentDrawable = (d == null) ? placeholder : d;
        mCurrentDrawable.setCallback(this);
        mRemoteState = new RemoteState();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mCurrentDrawable != null) {
            mCurrentDrawable.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (alpha != mRemoteState.mAlpha) {
            mRemoteState.mAlpha = alpha;
            if (mCurrentDrawable != null) {
                mCurrentDrawable.setAlpha(alpha);
            }
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf != mRemoteState.mColorFilter) {
            mRemoteState.mColorFilter = cf;
            if (mCurrentDrawable != null) {
                mCurrentDrawable.setColorFilter(cf);
            }
        }
    }

    @Override
    public void setDither(boolean dither) {
        if (mRemoteState.mDither != dither) {
            mRemoteState.mDither = dither;
            if (mCurrentDrawable != null) {
                mCurrentDrawable.setDither(dither);
            }
        }
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        if (mRemoteState.mFilter != filter) {
            mRemoteState.mFilter = filter;
            if (mCurrentDrawable != null) {
                mCurrentDrawable.setFilterBitmap(filter);
            }
        }
    }

    @Override
    public Drawable getCurrent() {
        return mCurrentDrawable;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        if (mCurrentDrawable != null) {
            mCurrentDrawable.setBounds(bounds);
        }
    }

    @Override
    public boolean isStateful() {
        if (mCurrentDrawable != null) {
            return mCurrentDrawable.isStateful();
        }
        return false;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        if (mCurrentDrawable != null) {
            return mCurrentDrawable.setState(state);
        }
        return false;
    }

    @Override
    protected boolean onLevelChange(int level) {
        if (mCurrentDrawable != null) {
            return mCurrentDrawable.setLevel(level);
        }
        return false;
    }

    @Override
    public int getIntrinsicWidth() {
        return mCurrentDrawable != null ? mCurrentDrawable.getIntrinsicWidth() : -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return mCurrentDrawable != null ? mCurrentDrawable.getIntrinsicHeight() : -1;
    }

    @Override
    public int getMinimumWidth() {
        return mCurrentDrawable != null ? mCurrentDrawable.getMinimumWidth() : 0;
    }

    @Override
    public int getMinimumHeight() {
        return mCurrentDrawable != null ? mCurrentDrawable.getMinimumHeight() : 0;
    }

    @Override
    public int getOpacity() {
        /*
         * Here PixelFormat.TRANSPARENT is returned because the getOpacity()
         * method has to be as conservative as possible. Indeed, we don't know
         * the opacity of the remote Drawable ...
         */
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public boolean getPadding(Rect padding) {
        return mCurrentDrawable == null ? false : mCurrentDrawable.getPadding(padding);
    }

    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mRemoteState = new RemoteState(mRemoteState);
            mMutated = true;
        }
        return this;
    }

    @Override
    public final ConstantState getConstantState() {
        mRemoteState.mChangingConfigurations = super.getChangingConfigurations();
        return mRemoteState;
    }

    final static class RemoteState extends Drawable.ConstantState {

        int mAlpha;
        ColorFilter mColorFilter;
        boolean mDither;
        boolean mFilter;

        int mChangingConfigurations;

        public RemoteState() {
            mAlpha = 0xFF;
            mColorFilter = null;
            mDither = true;
            mFilter = true;
        }

        public RemoteState(RemoteState state) {
            mAlpha = state.mAlpha;
            mColorFilter = state.mColorFilter;
            mDither = state.mDither;
            mFilter = state.mFilter;
            mChangingConfigurations = state.mChangingConfigurations;
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }

        @Override
        public Drawable newDrawable() {
            // TODO cyril: Should return a Drawable that is a copy of the
            // current Drawable (same placeholder as well as URL)
            return null;
        }

    }

    public void onDrawableLoadingFailed(String urlString) {
        if (DEBUG_LOGS_ENABLED) {
            Log.d(LOG_TAG, "onDrawableLoadingFailed: " + urlString);
        }
        // TODO cyril : What to do here? Doing nothing seems like a great idea
        // as it keep the placeholder...
    }

    public void onDrawableLoaded(String urlString, final Drawable drawable) {
        Rect bounds = mCurrentDrawable.getBounds();
        if (DEBUG_LOGS_ENABLED) {
            Log.d(LOG_TAG, "Bounds are : " + bounds);
        }
        drawable.setBounds(mCurrentDrawable.getBounds());
        mCurrentDrawable = drawable;
        invalidateSelf();
    }

    public void invalidateDrawable(Drawable who) {
        if (who == mCurrentDrawable) {
            invalidateSelf();
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }
}
