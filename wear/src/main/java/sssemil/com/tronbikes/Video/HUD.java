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

package sssemil.com.tronbikes.Video;

import android.content.Context;
import android.opengl.GLES10;

import sssemil.com.tronbikes.Game.GLTronGame;
import sssemil.com.tronbikes.R;

public class HUD {

    // fps members
    private final int FPS_HSIZE = 20;
    //private GL10 gl;
    // console members
    private final int CONSOLE_DEPTH = 100;
    private final Context mContext;
    private Font xenoTron;
    private int fps_h[] = new int[FPS_HSIZE];
    private int pos = -FPS_HSIZE;
    private int fps_min = 0;
    private int fps_avg = 0;
    private String consoleBuff[] = new String[CONSOLE_DEPTH];
    private int position;
    private int offset;

    // win lose
    private boolean dispWinner = false;
    private boolean dispLoser = false;
    private boolean dispInst = true;

    public HUD(Context ctx) {
        mContext = ctx;
        // Load font
        xenoTron = new Font(ctx, R.drawable.xenotron0, R.drawable.xenotron1);
        // Hard code these values for now allow loadable fonts later...
        xenoTron._texwidth = 256;
        xenoTron._width = 32;
        xenoTron._lower = 32;
        xenoTron._upper = 126;

        resetConsole();
    }

    public void draw(Video Visual, long dt, int plyrScore) {
        // Draw fps
        GLES10.glDisable(GLES10.GL_DEPTH_TEST);
        Visual.rasonly();

        if (GLTronGame.mPrefs.DrawFPS())
            drawFPS(Visual, dt);

        drawConsole(Visual);
        drawWinLose(Visual);
        drawScore(plyrScore);
        drawInstructions(Visual);
    }

    public void resetConsole() {
        int i;

        position = 0;
        offset = 0;

        for (i = 0; i < CONSOLE_DEPTH; i++) {
            consoleBuff[i] = null;
        }

        dispWinner = false;
        dispLoser = false;
    }

    public void displayWin() {
        if (!dispLoser)
            dispWinner = true;
    }

    public void displayLose() {
        if (!dispWinner)
            dispLoser = true;
    }

    public void displayInstr(Boolean value) {
        dispInst = value;
    }


    public void addLineToConsole(String str) {
        int i, x = 0;

        consoleBuff[position] = str;
        position++;

        if (position >= (CONSOLE_DEPTH - 1)) {
            // shuffle data up the array
            for (i = 0; i < CONSOLE_DEPTH; i++) {
                consoleBuff[x] = consoleBuff[i];
                ++x;
            }
            position -= 4;
        }
    }

    private void drawScore(int score) {
        GLES10.glColor4f(1.0f, 1.0f, 0.2f, 1.0f);
        xenoTron.drawText(60, 60, 24, mContext.getString(R.string.score) + score);
    }

    private void drawWinLose(Video Visual) {
        String str = null;

        if (dispWinner)
            str = mContext.getString(R.string.you_win);
        else if (dispLoser)
            str = mContext.getString(R.string.you_lose);

        if (str != null) {
            xenoTron.drawText(
                    5,
                    Visual._vp_h / 2,
                    (Visual._vp_w / (str.length())),
                    str);
        }

    }

    private void drawInstructions(Video Visual) {
        String str1;

        if (dispInst) {
            str1 = mContext.getString(R.string.tap_screen_to_start);

            GLES10.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            xenoTron.drawText(
                    5,
                    Visual._vp_h / 2,
                    (Visual._vp_w / (str1.length())),
                    str1);
        }
    }

    private void drawConsole(Video Visual) {
        int lines = 3; // lines of console to display
        int i;
        int index;

        for (i = 0; i < lines; i++) {
            index = (position + i - lines - offset + CONSOLE_DEPTH) % CONSOLE_DEPTH;
            if (consoleBuff[index] != null) {
                int size = 30;
                int length = consoleBuff[index].length();

                while (length * size > Visual._vp_w / 2 - 25)
                    size--;

                GLES10.glColor4f(1.0f, 0.4f, 0.2f, 1.0f);
                xenoTron.drawText(40, Visual.GetHeight() - 25 * (i + 1), size + 6, consoleBuff[index]);
            }
        }
    }

    private void drawFPS(Video Visual, long dt) {
        int diff;
        StringBuilder sb = new StringBuilder();

        diff = (dt > 0) ? (int) dt : 1;

        if (pos < 0) {
            fps_avg = 1000 / diff;
            fps_min = 1000 / diff;
            fps_h[pos + FPS_HSIZE] = 1000 / diff;
            pos++;
        } else {
            fps_h[pos] = 1000 / diff;
            pos = (pos + 1) % FPS_HSIZE;

            if (pos % 10 == 0) {
                int i;
                int sum = 0;
                int min = 1000;

                for (i = 0; i < FPS_HSIZE; i++) {
                    sum += fps_h[i];
                    if (fps_h[i] < min)
                        min = fps_h[i];
                }
                fps_min = min;
                fps_avg = sum / FPS_HSIZE;
            }
        }

        sb.append(mContext.getString(R.string.fps));
        sb.append(fps_avg);

        GLES10.glColor4f(1.0f, 0.4f, 0.2f, 1.0f);
        xenoTron.drawText(Visual.GetWidth() / 2, Visual.GetHeight() - 100, 20, sb.toString());
    }
}
