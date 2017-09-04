package com.yaxon.frameWork.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * 系统音量调节
 *
 * @author guojiaping
 * @version 2016/4/27 创建<br>
 */
public class SoundAdjustUtils {

    /**
     * 系统音量设置
     *
     * @param volume 音量值
     */
    public void setSystemVolume(Context context, int volume) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) return;
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        volume = Math.abs(volume);
        volume = (int) (volume * maxVolume / 100.0);
        if (volume > maxVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, maxVolume, 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0);
        }
    }

    /**
     * 音乐音量设置
     *
     * @param musicVolume 音量值
     */
    public void setMusicVolume(Context context, int musicVolume) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) return;
        musicVolume = Math.abs(musicVolume);
        int maxMusicVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        musicVolume = (int) (musicVolume * maxMusicVolume / 100.0);
        if (musicVolume > maxMusicVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusicVolume, 0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, musicVolume, 0);
        }
    }

}
