package com.codekong.vehiclenetworking.data;

import android.os.Environment;
import android.util.Log;

import com.codekong.vehiclenetworking.activity.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class LocalMusicList {

    private List<Map<String, String>> musiclist;
    private static String fileName="data.properties";
    private String initMusicName="/music/music.mp3";
    private static File sdcardDir = Environment.getExternalStorageDirectory();
    private static File file = new File(sdcardDir,fileName);
    private List<String> musicData;

    private void findAll(File file, List<File> list) {
        File[] subFiles = file.listFiles();
        if (subFiles != null)
            for (File subFile : subFiles) {
                if (subFile.isFile() && subFile.getName().endsWith(".mp3"))
                    list.add(subFile);
                else if (subFile.isDirectory())//如果是目录
                    findAll(subFile, list); //递归
            }
    }

   private void generateListView() {
        List<File> list = new ArrayList<File>();
        findAll(Environment.getExternalStorageDirectory(), list);//获取sdcard中的所有歌曲
        Collections.sort(list);  //播放列表进行排序，字符顺序

        musiclist = new ArrayList<Map<String, String>>();
        for (File file : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", file.getName());
            map.put("path", file.getAbsolutePath());
            musiclist.add(map);
        }
    }

   private void save(FileOutputStream fileOutputStream){
        Properties properties = new Properties();
        try {
            if(fileOutputStream!=null) {
                for(int i=0;i<musiclist.size();i++){
                    String name=musiclist.get(i).get("name");
                    String path=musiclist.get(i).get("path");
                    properties.setProperty(name, path);
                    properties.store(fileOutputStream, "保存文件");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       try {
           fileOutputStream.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

   private void read(FileInputStream inputStream) {
        Properties properties = new Properties();
        if (inputStream != null) {
            try {
                properties.load(inputStream);
                Set<Object> keySet = properties.keySet();
                musiclist = new ArrayList<>();
                for (Object object : keySet) {
                    String name = object.toString();
                    String path = properties.getProperty(object.toString());
                    Map<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("path", path);
                    musiclist.add(map);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Map<String, String>> getMusiclist(){
            return musiclist;
    }

    public static boolean isExist(){
        boolean isExist;
        isExist=file.exists();
        return isExist;
    }

    public LocalMusicList(int flag){}

    public LocalMusicList(){
        try {
            if(isExist()) {
                FileInputStream inputStream=new FileInputStream(file);
                read(inputStream);
                inputStream.close();
            }else{
                musiclist=new ArrayList<>();
                File initFile=new File(sdcardDir+initMusicName);
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", initFile.getName());
                map.put("path", initFile.getAbsolutePath());
                musiclist.add(map);
                Log.d("pyh", "LocalMusicList: " + musiclist.toString());
                FileOutputStream out=new FileOutputStream(file);
                save(out);
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getMusicData() {
        if(musiclist!=null) {
            musicData = new ArrayList<String>();
            for (int i = 0; i < musiclist.size(); i++) {
                String data = musiclist.get(i).get("name");
                data = data.replaceFirst(".mp3", "");
                musicData.add(data);
            }
        }
        return musicData;
    }

    public void scanLocalMusic(){
        generateListView();
        try {
            FileOutputStream outputStream=new FileOutputStream(file);
            save(outputStream);
            outputStream.close();

            MainActivity.loadingDialog.dismiss();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
