package com.wakkir.designpattern.structural.adapter;

/**
 * User: wakkir.muzammil
 * Date: 04/11/13
 * Time: 15:15
 */
public class AdapterPatternDemo
{
    public static void main(String[] args)
    {
        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.play("mp3", "beyond the horizon.mp3");
        audioPlayer.play("mp4", "alone.mp4");
        audioPlayer.play("vlc", "far far away.vlc");
        audioPlayer.play("avi", "mind me.avi");
    }
}