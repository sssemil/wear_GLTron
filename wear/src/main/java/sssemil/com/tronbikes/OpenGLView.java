/*
 * Copyright © 2012 Iain Churcher
 * Copyright © 2015 Emil Suleymanov <suleymanovemil8@gmail.com>
 *
 * Based on GLtron by Andreas Umbach (www.gltron.org)
 *
 * This file is part of GL TRON.
 *
 * GL TRON is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GL TRON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GL TRON.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package sssemil.com.tronbikes;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import sssemil.com.tronbikes.Game.GLTronGame;

public class OpenGLView extends GLSurfaceView implements GLSurfaceView.Renderer {

    GLTronGame Game;
    Context mContext;
    private int frameCount = 0;

    public OpenGLView(Context context) {
        super(context);
        Log.v(this.getClass().getName(), "View's constructor called");

        setRenderer(this);
        Game = new GLTronGame();
        mContext = context;

        Log.v(this.getClass().getName(), "All set for View");
    }

    public void onPause() {
        Log.v(this.getClass().getName(), "View Paused");
        Game.pauseGame();
    }

    public void onResume() {
        Log.v(this.getClass().getName(), "View Resumed");
        Game.resumeGame(mContext);
    }

    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Game.addTouchEvent(event.getX(), event.getY());
        }

        return true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(this.getClass().getName(), "Surface Created, Do perspective");

        Game.drawSplash(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        Log.d(this.getClass().getName(), "Surface changed, Update the game screen");
        Game.updateScreenSize(w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (frameCount == 1) {
            Log.v(this.getClass().getName(), "Drawing the first frame for the game");
            Game.initialiseGame();
        } else if (frameCount > 1) {
            Game.RunGame();
        }

        frameCount++;
    }
}
