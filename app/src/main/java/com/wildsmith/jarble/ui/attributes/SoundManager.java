package com.wildsmith.jarble.ui.attributes;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager implements MediaPlayer.OnCompletionListener {

    private static final String TAG = SoundManager.class.getSimpleName();

    private static final SoundManager INSTANCE = new SoundManager();

    private static SoundManager getInstance() {
        return INSTANCE;
    }

    private enum PlayingState {
        PLAYING,
        PAUSED,
        STOPPED
    }

    public SoundManager() {
        //singleton instance
    }

    private SparseArray<MediaPlayer> mediaPlayerMap = new SparseArray<>();

    private Map<Integer, PlayingState> mediaPlayerPlayingMap = new HashMap<>();

    public static void prepareSound(Context context, int resourceId, boolean looping) {
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setLooping(looping);

            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resourceId);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());

            afd.close();
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            Log.e(TAG, "An issue occurred while preparing the media player.", e);
        }

        getInstance().prepareMediaPlayer(resourceId, mediaPlayer);
    }

    private void prepareMediaPlayer(int resourceId, MediaPlayer mediaPlayer) {
        mediaPlayer.setOnCompletionListener(this);

        mediaPlayerMap.put(resourceId, mediaPlayer);
        mediaPlayerPlayingMap.put(resourceId, PlayingState.STOPPED);
    }

    public static void playSound(int soundId) {
        getInstance().playMediaPlayer(soundId);
    }

    private void playMediaPlayer(int soundId) {
        if (mediaPlayerMap == null || mediaPlayerMap.size() == 0) {
            return;
        }

        if (mediaPlayerPlayingMap == null || mediaPlayerPlayingMap.size() == 0) {
            return;
        }

        MediaPlayer mediaPlayer = mediaPlayerMap.get(soundId);
        if (mediaPlayer == null || PlayingState.PLAYING == mediaPlayerPlayingMap.get(soundId)) {
            return;
        }

        mediaPlayerPlayingMap.put(soundId, PlayingState.PLAYING);

        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.prepare();
            } catch (IllegalStateException | IOException e) {
                Log.e(TAG, "An issue occurred while preparing the sound for playing.", e);
            }

            mediaPlayer.start();
        }
    }

    public static void stopSound(int soundId) {
        getInstance().stopMediaPlayer(soundId);
    }

    private void stopMediaPlayer(int soundId) {
        if (mediaPlayerMap == null || mediaPlayerMap.size() == 0) {
            return;
        }

        if (mediaPlayerPlayingMap == null || mediaPlayerPlayingMap.size() == 0) {
            return;
        }

        MediaPlayer mediaPlayer = mediaPlayerMap.get(soundId);
        PlayingState playingState = mediaPlayerPlayingMap.get(soundId);
        if (mediaPlayer == null || PlayingState.PLAYING != playingState) {
            return;
        }

        mediaPlayer.stop();
        mediaPlayerPlayingMap.put(soundId, PlayingState.STOPPED);
    }

    public static void pauseSound(int soundId) {
        getInstance().pauseMediaPlayer(soundId);
    }

    private void pauseMediaPlayer(int soundId) {
        if (mediaPlayerMap == null || mediaPlayerMap.size() == 0) {
            return;
        }

        if (mediaPlayerPlayingMap == null || mediaPlayerPlayingMap.size() == 0) {
            return;
        }

        MediaPlayer mediaPlayer = mediaPlayerMap.get(soundId);
        PlayingState playingState = mediaPlayerPlayingMap.get(soundId);
        if (mediaPlayer == null || PlayingState.PLAYING != playingState) {
            return;
        }

        mediaPlayer.pause();
        mediaPlayerPlayingMap.put(soundId, PlayingState.PAUSED);
    }

    public static void pauseSounds() {
        getInstance().pauseMediaPlayers();
    }

    private void pauseMediaPlayers() {
        if (mediaPlayerMap == null || mediaPlayerMap.size() == 0) {
            return;
        }

        if (mediaPlayerPlayingMap == null || mediaPlayerPlayingMap.size() == 0) {
            return;
        }

        for (int i = 0; i < mediaPlayerMap.size(); i++) {
            Integer soundId = mediaPlayerMap.keyAt(i);
            MediaPlayer mediaPlayer = mediaPlayerMap.valueAt(i);
            PlayingState playingState = mediaPlayerPlayingMap.get(soundId);
            if (mediaPlayer == null || PlayingState.PLAYING != playingState) {
                return;
            }

            mediaPlayer.pause();
            mediaPlayerPlayingMap.put(soundId, PlayingState.PAUSED);
        }
    }

    public static void stopSounds() {
        getInstance().stopMediaPlayers();
    }

    private void stopMediaPlayers() {
        if (mediaPlayerMap == null || mediaPlayerMap.size() == 0) {
            return;
        }

        if (mediaPlayerPlayingMap == null || mediaPlayerPlayingMap.size() == 0) {
            return;
        }

        for (int i = 0; i < mediaPlayerMap.size(); i++) {
            Integer soundId = mediaPlayerMap.keyAt(i);
            MediaPlayer mediaPlayer = mediaPlayerMap.valueAt(i);
            PlayingState playingState = mediaPlayerPlayingMap.get(soundId);
            if (mediaPlayer == null || PlayingState.PLAYING != playingState) {
                return;
            }

            mediaPlayer.stop();
            mediaPlayerPlayingMap.put(soundId, PlayingState.STOPPED);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            return;
        }

        mediaPlayer.stop();
        mediaPlayer.reset();

        for (int i = 0; i < mediaPlayerMap.size(); i++) {
            final int key = mediaPlayerMap.keyAt(i);
            // get the object by the key.
            final MediaPlayer storedMediaPlayer = mediaPlayerMap.get(key);
            if (mediaPlayer == storedMediaPlayer) {
                mediaPlayerPlayingMap.put(key, PlayingState.STOPPED);
            }
        }
    }
}