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

package sssemil.com.tronbikes.Sound;

import android.content.Context;
import android.media.*;
import android.util.Log;
import sssemil.com.tronbikes.R;

import java.io.IOException;
import java.util.HashMap;

public class SoundManager {

    // sound index
    public static int CRASH_SOUND = 1;
    public static int ENGINE_SOUND = 2;
    public static int MUSIC_SOUND = 3;
    public static int RECOGNIZER_SOUND = 4;

    private static final int MAX_SOUNDS = 10;
    // music
    private MediaPlayer mPlayer;
    // sound effects
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap;
    private AudioManager mAudioManager;
    private Context mContext;
    private static int MAX_INDEX = 10;

    private int mIndexToStream[] = new int[MAX_INDEX];

    private boolean mSupportsAudio = true;

    private SoundManager(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap<>();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AudioDeviceInfo[] adi = mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            if (adi.length == 0) {
                mSupportsAudio = false;
            } else {
                for (AudioDeviceInfo audioDeviceInfo : adi) {
                    Log.e((String) audioDeviceInfo.getProductName(), String.valueOf(audioDeviceInfo.getId()));
                }
            }
        }
        addSound(ENGINE_SOUND, R.raw.game_engine);
        addSound(CRASH_SOUND, R.raw.game_crash);
        addSound(RECOGNIZER_SOUND, R.raw.game_recognizer);
        addMusic(R.raw.song_revenge_of_cats);
    }

    /*
     * Requests the instance if the sound manager and creates it
     * if it does not exist
     */
    static synchronized public SoundManager getInstance(Context context) {
        return new SoundManager(context);
    }

    private void addSound(int Index, int SoundID) {
        if (mSupportsAudio) {
            mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
        }
    }

    private void addMusic(int MusicID) {
        // limited to one music stream FIXME
        if (mSupportsAudio) {
            mPlayer = MediaPlayer.create(mContext, MusicID);
        }
    }

    public void playMusic(boolean boLoop) {
        if (mSupportsAudio && mPlayer != null) {
            float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            streamVolume /= mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            mPlayer.setLooping(boLoop);
            mPlayer.setVolume(streamVolume, streamVolume);
            mPlayer.start();
        }
    }


    public void stopMusic() {
        if (mSupportsAudio && mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            try {
                mPlayer.prepare();
                mPlayer.seekTo(0);
            } catch (IOException e) {
                // do nothing here FIXME
            }
        }
    }

    public void playSound(int index, float speed) {
        if (mSupportsAudio && mSoundPool!=null) {
            float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            streamVolume /= mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            streamVolume *= 0.5;
            mIndexToStream[index] = mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume,1,0, speed);
        }
    }

    public void playSoundLoop(int index, float speed) {
        if (mSupportsAudio && mSoundPool!=null) {
            float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            streamVolume /= mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            streamVolume *= 0.5;
            mIndexToStream[index] = mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, speed);
        }
    }

    public void stopSound(int index) {
        //mSoundPool.stop(mSoundPoolMap.get(index));
        if (mSupportsAudio) {
            mSoundPool.stop(mIndexToStream[index]);
        }
    }

    public void changeRate(int index, float rate) {
        //mSoundPool.setRate(mSoundPoolMap.get(index), rate);
        if (mSupportsAudio) {
            mSoundPool.setRate(mIndexToStream[index], rate);
        }
    }

    public void globalPauseSound() {
        if (mSupportsAudio) {
            if (mSoundPool != null)
                mSoundPool.autoPause();

            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.pause();
        }
    }

    public void globalResumeSound() {
        if (mSupportsAudio) {
            if (mSoundPool != null)
                mSoundPool.autoResume();

            if (mPlayer != null)
                mPlayer.start();
        }
    }

    public void cleanup() {
        mSoundPool.release();
        mSoundPool = null;
        mSoundPoolMap.clear();
        mAudioManager.unloadSoundEffects();
    }
}