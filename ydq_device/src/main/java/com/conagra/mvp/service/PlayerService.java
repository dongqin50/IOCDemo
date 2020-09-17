package com.conagra.mvp.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.conagra.R;
import com.conagra.hardware.AppConstant;
import com.conagra.hardware.activity.PlayerActivity;
import com.conagra.hardware.model.LrcContent;
import com.conagra.hardware.model.LrcProcess;
import com.conagra.hardware.model.Mp3Info;
import com.conagra.mvp.utils.MediaUtil;

import java.util.ArrayList;
import java.util.List;


/***
 * 2013/5/25
 * @author wwj
 * ���ֲ��ŷ���
 */
@SuppressLint("NewApi")
public class PlayerService extends Service {
	private MediaPlayer mediaPlayer;
	private String path;
	private int msg;
	private boolean isPause;
	private int current = 0;
	private List<Mp3Info> mp3Infos;
	private int status = 3;
	private MyReceiver myReceiver;
	private int currentTime;
	private int duration;			//���ų���
	private LrcProcess mLrcProcess;	//��ʴ���
	private List<LrcContent> lrcList = new ArrayList<LrcContent>(); //��Ÿ���б����
	private int index = 0;			//��ʼ���ֵ
	
	//����Ҫ���͵�һЩAction
	public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION";	//���¶���
	public static final String CTL_ACTION = "com.wwj.action.CTL_ACTION";		//���ƶ���
	public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT";	//��ǰ���ֲ���ʱ����¶���
	public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION";//�����ֳ��ȸ��¶���
	public static final String SHOW_LRC = "com.wwj.action.SHOW_LRC";			//֪ͨ��ʾ���
	
	/**
	 * handler����������Ϣ�������͹㲥���²���ʱ��
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if(mediaPlayer != null) {
					currentTime = mediaPlayer.getCurrentPosition(); // ��ȡ��ǰ���ֲ��ŵ�λ��
					Intent intent = new Intent();
					intent.setAction(MUSIC_CURRENT);
					intent.putExtra("currentTime", currentTime);
					sendBroadcast(intent); // ��PlayerActivity���͹㲥
					handler.sendEmptyMessageDelayed(1, 1000);
				}
			}
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "service created");
		mediaPlayer = new MediaPlayer();
		mp3Infos = MediaUtil.getMp3Infos(PlayerService.this);

		/**
		 * �������ֲ������ʱ�ļ�����
		 */
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				if (status == 1) { // ����ѭ��
					mediaPlayer.start();
				} else if (status == 2) { // ȫ��ѭ��
					current++;
					if(current > mp3Infos.size() - 1) {	//��Ϊ��һ�׵�λ�ü�������
						current = 0;
					}
					Intent sendIntent = new Intent(UPDATE_ACTION);
					sendIntent.putExtra("current", current);
					// ���͹㲥������Activity����е�BroadcastReceiver���յ�
					sendBroadcast(sendIntent);
					path = mp3Infos.get(current).getUrl();
					play(0);
				} else if (status == 3) { // ˳�򲥷�
					current++;	//��һ��λ��
					if (current <= mp3Infos.size() - 1) {
						Intent sendIntent = new Intent(UPDATE_ACTION);
						sendIntent.putExtra("current", current);
						// ���͹㲥������Activity����е�BroadcastReceiver���յ�
						sendBroadcast(sendIntent);
						path = mp3Infos.get(current).getUrl();
						play(0);
					}else {
						mediaPlayer.seekTo(0);
						current = 0;
						Intent sendIntent = new Intent(UPDATE_ACTION);
						sendIntent.putExtra("current", current);
						// ���͹㲥������Activity����е�BroadcastReceiver���յ�
						sendBroadcast(sendIntent);
					}
				} else if(status == 4) {	//�������
					current = getRandomIndex(mp3Infos.size() - 1);
					System.out.println("currentIndex ->" + current);
					Intent sendIntent = new Intent(UPDATE_ACTION);
					sendIntent.putExtra("current", current);
					// ���͹㲥������Activity����е�BroadcastReceiver���յ�
					sendBroadcast(sendIntent);
					path = mp3Infos.get(current).getUrl();
					play(0);
				}
			}
		});

		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CTL_ACTION);
		filter.addAction(SHOW_LRC);
		registerReceiver(myReceiver, filter);
	}

	/**
	 * ��ȡ���λ��
	 * @param end
	 * @return
	 */
	protected int getRandomIndex(int end) {
		int index = (int) (Math.random() * end);
		return index;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		path = intent.getStringExtra("url");		//����·��
		current = intent.getIntExtra("listPosition", -1);	//��ǰ���Ÿ�������mp3Infos��λ��
		msg = intent.getIntExtra("MSG", 0);			//������Ϣ
		if (msg == AppConstant.PlayerMsg.PLAY_MSG) {	//ֱ�Ӳ�������
			play(0);
		} else if (msg == AppConstant.PlayerMsg.PAUSE_MSG) {	//��ͣ
			pause();	
		} else if (msg == AppConstant.PlayerMsg.STOP_MSG) {		//ֹͣ
			stop();
		} else if (msg == AppConstant.PlayerMsg.CONTINUE_MSG) {	//��������
			resume();	
		} else if (msg == AppConstant.PlayerMsg.PRIVIOUS_MSG) {	//��һ��
			previous();
		} else if (msg == AppConstant.PlayerMsg.NEXT_MSG) {		//��һ��
			next();
		} else if (msg == AppConstant.PlayerMsg.PROGRESS_CHANGE) {	//���ȸ���
			currentTime = intent.getIntExtra("progress", -1);
			play(currentTime);
		} else if (msg == AppConstant.PlayerMsg.PLAYING_MSG) {
			handler.sendEmptyMessage(1);
		}
		super.onStart(intent, startId);
	}

	/**
	 * ��ʼ���������
	 */
	public void initLrc(){
		mLrcProcess = new LrcProcess();
		//��ȡ����ļ�
		mLrcProcess.readLRC(mp3Infos.get(current).getUrl());
		//���ش����ĸ���ļ�
		lrcList = mLrcProcess.getLrcList();
		PlayerActivity.lrcView.setmLrcList(lrcList);
		//�л���������ʾ���
		PlayerActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(PlayerService.this, R.anim.alpha_z));
		handler.post(mRunnable);
	}
	Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			PlayerActivity.lrcView.setIndex(lrcIndex());
			PlayerActivity.lrcView.invalidate();
			handler.postDelayed(mRunnable, 100);
		}
	};
	
	/**
	 * ����ʱ���ȡ�����ʾ������ֵ
	 * @return
	 */
	public int lrcIndex() {
		if(mediaPlayer.isPlaying()) {
			currentTime = mediaPlayer.getCurrentPosition();
			duration = mediaPlayer.getDuration();
		}
		if(currentTime < duration) {
			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
						index = i;
					}
					if (currentTime > lrcList.get(i).getLrcTime()
							&& currentTime < lrcList.get(i + 1).getLrcTime()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1
						&& currentTime > lrcList.get(i).getLrcTime()) {
					index = i;
				}
			}
		}
		return index;
	}
	/**
	 * ��������
	 * 
	 * @param
	 */
	private void play(int currentTime) {
		try {
			initLrc();
			mediaPlayer.reset();// �Ѹ�������ָ�����ʼ״̬
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); // ���л���
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// ע��һ��������
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ͣ����
	 */
	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	private void resume() {
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}

	/**
	 * ��һ��
	 */
	private void previous() {
		Intent sendIntent = new Intent(UPDATE_ACTION);
		sendIntent.putExtra("current", current);
		// ���͹㲥������Activity����е�BroadcastReceiver���յ�
		sendBroadcast(sendIntent);
		play(0);
	}

	/**
	 * ��һ��
	 */
	private void next() {
		Intent sendIntent = new Intent(UPDATE_ACTION);
		sendIntent.putExtra("current", current);
		// ���͹㲥������Activity����е�BroadcastReceiver���յ�
		sendBroadcast(sendIntent);
		play(0);
	}

	/**
	 * ֹͣ����
	 */
	private void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare(); // �ڵ���stop�������Ҫ�ٴ�ͨ��start���в���,��Ҫ֮ǰ����prepare����
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		handler.removeCallbacks(mRunnable);
	}

	/**
	 * 
	 * ʵ��һ��OnPrepareLister�ӿ�,������׼���õ�ʱ��ʼ����
	 * 
	 */
	private final class PreparedListener implements MediaPlayer.OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); // ��ʼ����
			if (currentTime > 0) { // ������ֲ��Ǵ�ͷ����
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			intent.setAction(MUSIC_DURATION);
			duration = mediaPlayer.getDuration();
			intent.putExtra("duration", duration);	//ͨ��Intent�����ݸ������ܳ���
			sendBroadcast(intent);
		}
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int control = intent.getIntExtra("control", -1);
			switch (control) {
			case 1:
				status = 1; // ������״̬��Ϊ1��ʾ������ѭ��
				break;
			case 2:
				status = 2;	//������״̬��Ϊ2��ʾ��ȫ��ѭ��
				break;
			case 3:
				status = 3;	//������״̬��Ϊ3��ʾ��˳�򲥷�
				break;
			case 4:
				status = 4;	//������״̬��Ϊ4��ʾ���������
				break;
			}
			
			String action = intent.getAction();
			if(action.equals(SHOW_LRC)){
				current = intent.getIntExtra("listPosition", -1);
				initLrc();
			}
		}
	}

}
