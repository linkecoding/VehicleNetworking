package com.codekong.vehiclenetworking.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codekong.vehiclenetworking.R;
import com.codekong.vehiclenetworking.data.LocalMusicList;
import com.codekong.vehiclenetworking.service.IMusicService;
import com.codekong.vehiclenetworking.service.MusicService;

import java.util.List;

public class MusicFragment extends Fragment implements View.OnClickListener {
	private TextView nameTextView;

	private SeekBar seekBar;
	private ListView listView;
	public List<String> musicData;
	private boolean autoChange=true, manulChange;// 判断是进度条是自动改变还是手动改变
	private MusicHandler musicHandler;// 处理改变进度条事件
	private MusicThread musicThread;// 自动改变进度条的线程

	private TextView curTime, totalTime;

	private Myconn conn;
	private MusicService musicService;
	private IMusicService iMusicService;
	private ImageButton button_next, button_previous;
	private Button pause_play;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.musicpage, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Activity mActivity = getActivity();

		nameTextView = (TextView) mActivity.findViewById(R.id.name);
		seekBar = (SeekBar) mActivity.findViewById(R.id.seekbar);
		listView=(ListView)mActivity.findViewById(R.id.musiclist);
		curTime = (TextView) mActivity.findViewById(R.id.curTime);
		totalTime = (TextView) mActivity.findViewById(R.id.totalTime);
		pause_play=(Button)mActivity.findViewById(R.id.pause_play);
		button_next= (ImageButton) mActivity.findViewById(R.id.next);
		button_previous= (ImageButton) mActivity.findViewById(R.id.previous);

		Intent intent = new Intent(mActivity, MusicService.class); //保证服务长期运行
		mActivity.startService(intent); //调用服务的方法
		conn = new Myconn();
		mActivity.bindService(intent, conn, mActivity.BIND_AUTO_CREATE);

		musicData=new LocalMusicList().getMusicData();  //获得音乐数据
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, musicData);
		listView.setAdapter(adapter);

		pause_play.setOnClickListener(this);
		button_next.setOnClickListener(this);
		button_previous.setOnClickListener(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					pause_play.setText("||");
					musicService.play(position);
				}
		});
		musicHandler=new MusicHandler();
		musicThread=new MusicThread();
		new Thread(musicThread).start();

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					int progress = seekBar.getProgress();
					autoChange = false;
					manulChange = true;
					if (!autoChange && manulChange) {
						int musicMax = musicService.getMediaPlayer().getDuration(); //得到该首歌曲最长秒数
						int seekBarMax = seekBar.getMax();

						musicService.getMediaPlayer().seekTo(musicMax * progress / seekBarMax);//跳到该曲该秒
						autoChange = true;
						manulChange = false;
					}
				}
			});
		}

	private void pause_play(){
		if ("||".equals(pause_play.getText())) {
			musicService.pause();
			pause_play.setText("▶");
		} else {
			musicService.resume();
			pause_play.setText("||");
		}

	}

	public void next() {
		musicService.next();
		pause_play.setText("||");
	}


	public void previous() {
		if (musicService != null){
			pause_play.setText("||");
			musicService.previous();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.previous:
				previous();
				break;
			case R.id.pause_play:
				Log.d("pyh", "onClick: ININ");
				pause_play();
				break;
			case R.id.next:
				next();
				break;
			default:
				break;
		}

	}


	private class Myconn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iMusicService = (IMusicService) service;
			musicService = iMusicService.callGetMusicService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			musicService=null;
		}
	}

	class MusicHandler extends Handler {

		public MusicHandler() {
		}

		@Override
		public void handleMessage(Message msg) {
			if (autoChange) {
				try {
					int position = musicService.getMediaPlayer().getCurrentPosition();//得到当前歌曲播放进度(秒)
					int mMax = musicService.getMediaPlayer().getDuration();//最大秒数
					int sMax = seekBar.getMax();//seekBar最大值，算百分比
					seekBar.setProgress(position * sMax / mMax);
					nameTextView.setText(setPlayInfo(position / 1000, mMax / 1000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				seekBar.setProgress(0);
				nameTextView.setText("播放已经停止");
			}
		}
	}

	//设置当前播放的信息
	private String setPlayInfo(int position, int max) {
		String info =musicService.getMusicName();
		info=info.replaceFirst(".mp3","");
		int pMinutes = 0;
		while (position >= 60) {
			pMinutes++;
			position -= 60;
		}
		String now = (pMinutes < 10 ? "0" + pMinutes : pMinutes) + ":"
				+ (position < 10 ? "0" + position : position);

		int mMinutes = 0;
		while (max >= 60) {
			mMinutes++;
			max -= 60;
		}

		String all = (mMinutes < 10 ? "0" + mMinutes : mMinutes) + ":"+ (max < 10 ? "0" + max : max);
		curTime.setText(now);
		totalTime.setText(all);
		return info;
	}

	class MusicThread implements Runnable {
		@Override
		public void run() {
			while (true)
				try {
					musicHandler.sendMessage(new Message());
					Thread.sleep(1000);// 每间隔1秒发送一次更新消息
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		musicService.onDestroy();
	}
}
