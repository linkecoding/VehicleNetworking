package com.codekong.vehiclenetworking.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.data.LocalMusicList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MusicService extends Service {
    private static final int PAUSE = 0;
    private static final int PLAYING = 1;
    private static int PLAY_STATUS  ;
    private MediaPlayer mediaPlayer;
    private String path;//要播放的音乐路径
    private List<Map<String, String>> musiclist;

    private int current;
    private class MyBinder extends Binder implements IMusicService {

        @Override
        public MusicService callGetMusicService() {
            return getMusicService();
        }

    }
    /*
     * 返回服务对象
     */
    public MusicService getMusicService(){
        return this;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.reset(); //重置一下服务器，恢复出厂设置
        File file=new File(Environment.getExternalStorageDirectory()+"/music/music.mp3");
        if (!file.exists()){
            saveMusicToLocal();
        }
        musiclist=new LocalMusicList().getMusiclist();
        Log.d("pyh", "onCreate: "+musiclist.toString());
        if (musiclist.size() != 0) {
            setMusicPath(musiclist.get(0).get("path"));
            play();
        }
    }

    private void saveMusicToLocal() {
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(R.raw.music);
            byte[] reader = new byte[inputStream.available()];
            while (inputStream.read(reader) != -1) {}
            File file=new File(Environment.getExternalStorageDirectory()+"/music");
            if(!file.exists()){
                file.mkdir();
            }
            String path=Environment.getExternalStorageDirectory()+"/music/music.mp3";
            file=new File(path);
            if (!file.exists()){
                writefile(reader,path);
                Log.d("pyh","music.mp3");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writefile(byte[] str,String path)
    {
        File file;
        FileOutputStream out;
        try {
            //创建文件
            file = new File(path);
            file.createNewFile();
            //打开文件file的OutputStream
            out = new FileOutputStream(file);

            //将字符串转换成byte数组写入文件
            out.write(str);
            //关闭文件file的OutputStream
            out.close();
        } catch (IOException e) {
            //将出错信息打印到Logcat
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    /*
    * 设置要播放的音乐
    * 音乐文件的路径
    */
    public void setMusicPath(String path){
        this.path=path;
    }
    /*
     *播放音乐
     */
    public boolean play(){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        try {
            if(path==null){
                return false;
            }
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                        current = (current + 1) % musiclist.size();
                    setMusicPath(musiclist.get(current).get("path"));
                        play();
                }
            });
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*
     * 播放选择的音乐
     */
    public boolean play(int position){
        current=position;
        setMusicPath(musiclist.get(current).get("path"));
        return play();
    }
    /*
     * 播放下一首音乐
     */
    public void next(){
        current = (current + 1) % musiclist.size();
        setMusicPath(musiclist.get(current).get("path"));
        play();
    }
    /*
     * 播放上一首音乐
     */
    public void previous(){
        current = current - 1 < 0 ? musiclist.size() - 1 : current - 1;
        setMusicPath(musiclist.get(current).get("path"));
        play();
    }

    /*
     * 暂停音乐
     */
    public void pause(){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    /*
     * 继续开始
     */
    public void resume(){
        if(mediaPlayer!=null&&PLAY_STATUS==PAUSE){
            mediaPlayer.start();
            PLAY_STATUS=PLAYING;
        }
    }

    public String getMusicName(){
        String musicName=musiclist.get(current).get("name");
        return musicName;
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }


}
