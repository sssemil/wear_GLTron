/*
 * Copyright © 2012 Iain Churcher
 * Copyright © 2015 Emil Suleymanov <suleymanovemil8@gmail.com>
 *
 * Based on GLtron by Andreas Umbach (www.gltron.org)
 * 
 * Preferences implementation based on work by Noah NZM TECH
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

package sssemil.com.tronbikes.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPrefs {

    private static final float C_GRID_SIZES[] = {360.0f, 720.0f, 1440.0f};
    private static final float C_SPEED[] = {5.0f, 10.0f, 15.0f, 20.0f};
    // Pref defaults
    private final int C_PREF_FOLLOW_CAM = 1;
    private final int C_PREF_FOLLOW_CAM_FAR = 2;
    private final int C_PREF_FOLLOW_CAM_CLOSE = 3;
    private final int C_PREF_BIRD_CAM = 4;
    private final String C_DEFAULT_CAM_TYPE = "1";
    private Context mContext;
    private Camera.CamType mCameraType;

    private boolean mMusic;
    private boolean mSFX;

    private boolean mFPS;
    private boolean mDrawRecog;

    private int mNumOfPlayers;
    private float mGridSize;
    private float mSpeed;
    private int mPlayerColourIndex;

    public UserPrefs(Context ctx) {
        mContext = ctx;
        ReloadPrefs();
    }

    public void ReloadPrefs() {
        int cameraType;
        int gridIndex;
        int speedIndex;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        cameraType = Integer.valueOf(prefs.getString("cameraPref", C_DEFAULT_CAM_TYPE));

        switch (cameraType) {
            case C_PREF_FOLLOW_CAM:
                mCameraType = Camera.CamType.E_CAM_TYPE_FOLLOW;
                break;
            case C_PREF_FOLLOW_CAM_FAR:
                mCameraType = Camera.CamType.E_CAM_TYPE_FOLLOW_FAR;
                break;
            case C_PREF_FOLLOW_CAM_CLOSE:
                mCameraType = Camera.CamType.E_CAM_TYPE_FOLLOW_CLOSE;
                break;
            case C_PREF_BIRD_CAM:
                mCameraType = Camera.CamType.E_CAM_TYPE_BIRD;
                break;
            default:
                mCameraType = Camera.CamType.E_CAM_TYPE_FOLLOW;
                break;
        }

        mMusic = prefs.getBoolean("musicOption", true);
        mSFX = prefs.getBoolean("sfxOption", true);
        mFPS = prefs.getBoolean("fpsOption", false);
        mNumOfPlayers = Integer.valueOf(prefs.getString("playerNumber", "4"));
        gridIndex = Integer.valueOf(prefs.getString("arenaSize", "2"));
        mGridSize = C_GRID_SIZES[gridIndex];
        speedIndex = Integer.valueOf(prefs.getString("gameSpeed", "1"));
        mSpeed = C_SPEED[speedIndex];
        mPlayerColourIndex = Integer.valueOf(prefs.getString("playerBike", "0"));
        mDrawRecog = prefs.getBoolean("drawRecog", true);
    }

    public Camera.CamType CameraType() {

        return mCameraType;
    }

    public boolean PlayMusic() {
        return mMusic;
    }

    public boolean PlaySFX() {
        return mSFX;
    }

    public boolean DrawFPS() {
        return mFPS;
    }

    public int NumberOfPlayers() {
        return mNumOfPlayers;
    }

    public float GridSize() {
        return mGridSize;
    }

    public float Speed() {
        return mSpeed;
    }

    public int PlayerColourIndex() {
        return mPlayerColourIndex;
    }

    public boolean DrawRecognizer() {
        return mDrawRecog;
    }
}
