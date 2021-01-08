package com.palteam.shabbik.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.palteam.shabbik.R;

public class Sounds {
    static SoundPool sp;
    static int soundIds[] = new int[10];
    public int streamid;
    private int id;
    private Context context;
    private boolean soundFlag;
    private boolean isPaly;
    private boolean isPause;

    public Sounds(Context context, int id) {
        this.context = context;
        isPaly = false;
        this.id = id;
    }

    public static void intial(Context context) {

        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundIds[0] = sp.load(context, R.raw.wrongsound, 1);
        soundIds[1] = sp.load(context, R.raw.buttoneffect, 1);
        soundIds[2] = sp.load(context, R.raw.buttoneffect, 1);
        soundIds[3] = sp.load(context, R.raw.win, 1);
        soundIds[4] = sp.load(context, R.raw.negative_round, 1);
        soundIds[5] = sp.load(context, R.raw.icecracking, 1);
        soundIds[6] = sp.load(context, R.raw.icehitground, 1);
        soundIds[7] = sp.load(context, R.raw.gallops, 1);
        soundIds[8] = sp.load(context, R.raw.back, 1);
        soundIds[9] = sp.load(context, R.raw.button_press, 1);
    }

    // soundID a soundID returned by the load() function
    //
    // leftVolume left volume value (range = 0.0 to 1.0)
    //
    // rightVolume right volume value (range = 0.0 to 1.0)
    //
    // priority stream priority (0 = lowest priority)
    //
    // loop loop mode (0 = no loop, -1 = loop forever)
    //
    // rate playback rate (1.0 = normal playback, range 0.5 to 2.0)

    public void play(int loop,Context context) {
        if (sp != null && isSilent(context)) {
            streamid = sp.play(soundIds[this.id], 1, 1, 1, loop, 1);
            isPaly = true;
        }

    }

    public void stop() {
        if (sp != null && isPaly) {
            sp.stop(streamid);
            isPaly = false;

        }
    }

    public void pauseSound() {
        if (sp != null && !isPause && isPaly) {
            sp.pause(streamid);

            isPause = true;
        }
    }

    public void resumeSound() {
        if (sp != null && isPause) {
            sp.resume(streamid);
            isPause = false;
        }
    }

    public void releaseSound() {
        sp.release();
    }

    public void unloadSound() {
        sp.unload(streamid);
    }

    public boolean isSilent(Context context) {
        boolean soundFlag = false;
        AudioManager audio = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        switch (audio.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                soundFlag = true;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                soundFlag = false;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                soundFlag = false;
                break;
        }
        return soundFlag;
    }

    public void setMute() {
        sp.setVolume(streamid, 0, 0);
    }

    public void setUnMute() {
        sp.setVolume(streamid, 1, 1);
    }
}
