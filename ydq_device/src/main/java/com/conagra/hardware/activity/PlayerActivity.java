package com.conagra.hardware.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.conagra.R;
import com.conagra.hardware.AppConstant;
import com.conagra.hardware.model.LrcView;
import com.conagra.hardware.model.Mp3Info;
import com.conagra.mvp.service.PlayerService;
import com.conagra.mvp.utils.MediaUtil;

import java.util.List;

import static com.conagra.mvp.service.PlayerService.CTL_ACTION;


public class PlayerActivity extends Activity {
	private Button playBtn;
	private SeekBar music_progressBar;
	private TextView currentProgress; 
	private TextView finalProgress;
	private String title;
	private String url;
	private int listPosition;
	private int currentTime;
	private int duration;
	private int flag;

	private boolean isPlaying;
	private boolean isPause;
	private boolean isShuffle;

	private List<Mp3Info> mp3Infos;
	public static LrcView lrcView;

	private PlayerReceiver playerReceiver;
	public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION";
	public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT";
	public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION";
	public static final String MUSIC_PLAYING = "com.wwj.action.MUSIC_PLAYING";
	public static final String REPEAT_ACTION = "com.wwj.action.REPEAT_ACTION";
	public static final String SHUFFLE_ACTION = "com.wwj.action.SHUFFLE_ACTION";
	public static final String SHOW_LRC = "com.wwj.action.SHOW_LRC";

//	private TempleHeaderView mHeader;
	private AudioManager am;
	int currentVolume;
	int maxVolume;
//	private ImageView musicAlbum;
//	private ImageView musicAblumReflection;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("PlayerActivity onCreated");
		setContentView(R.layout.play_activity_layout);
		findViewById();
		setViewOnclickListener();
		getDataFromBundle();

		mp3Infos = MediaUtil.getMp3Infos(PlayerActivity.this);
		registerReceiver();

		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobliePhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		initView();
		am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
		System.out.println("currentVolume--->"+currentVolume);
		System.out.println("maxVolume-->" + maxVolume);

	}

	private void registerReceiver() {
		playerReceiver = new PlayerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_ACTION);
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		registerReceiver(playerReceiver, filter);
	}


	private void findViewById() {
		playBtn = (Button) findViewById(R.id.play_music);
		music_progressBar = (SeekBar) findViewById(R.id.audioTrack);
		currentProgress = (TextView) findViewById(R.id.current_progress);
		finalProgress = (TextView) findViewById(R.id.final_progress);
//		lrcView = (LrcView) findViewById(R.id.lrcShowView);
//		musicAlbum = (ImageView) findViewById(R.id.iv_music_ablum);
//		musicAblumReflection = (ImageView) findViewById(R.id.iv_music_ablum_reflection);
//		mHeader = (TempleHeaderView) findViewById(R.id.player_header);
	}

	private void setViewOnclickListener() {
		ViewOnclickListener ViewOnClickListener = new ViewOnclickListener();
		playBtn.setOnClickListener(ViewOnClickListener);
		music_progressBar
				.setOnSeekBarChangeListener(new SeekBarChangeListener());
	}

	private class MobliePhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
				playBtn.setBackgroundResource(R.drawable.play_selector);
				intent.setAction("com.dieke.feldsher.MUSIC_SERVICE");
				intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
				startService(intent);
				isPlaying = false;
				isPause = true;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
			case TelephonyManager.CALL_STATE_RINGING:
				Intent intent2 = new Intent(PlayerActivity.this, PlayerService.class);
				playBtn.setBackgroundResource(R.drawable.pause_selector);
				intent2.setAction("com.dieke.feldsher.MUSIC_SERVICE");
				intent2.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
				startService(intent2);
				isPlaying = true;
				isPause = false;
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("PlayerActivity has started");
	}

	private void getDataFromBundle() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if(bundle != null){
			title = bundle.getString("title");
			url = bundle.getString("url");
			isShuffle = bundle.getBoolean("shuffleState");
			flag = bundle.getInt("MSG");
			currentTime = bundle.getInt("currentTime");
			duration = bundle.getInt("duration");
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("PlayerActivity has paused");
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
		System.out.println("PlayerActivity has onResume");
	}

	/**
	 * ��ʼ������
	 */
	public void initView() {
		isPlaying = true;
		isPause = false;
//		mHeader.setTitle(title);
		music_progressBar.setProgress(currentTime);
		music_progressBar.setMax(duration);
		if (flag == AppConstant.PlayerMsg.PLAYING_MSG) {
			Toast.makeText(PlayerActivity.this, "���ڲ���--" + title, Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setAction(SHOW_LRC);
			intent.putExtra("listPosition", listPosition);
			sendBroadcast(intent);
		} else if (flag == AppConstant.PlayerMsg.PLAY_MSG) {
			playBtn.setBackgroundResource(R.drawable.play_selector);
			play();
		} else if (flag == AppConstant.PlayerMsg.CONTINUE_MSG) {
			Intent intent = new Intent(PlayerActivity.this, PlayerService.class);
			playBtn.setBackgroundResource(R.drawable.play_selector);
			intent.setAction("com.dieke.feldsher.MUSIC_SERVICE");
			intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
			startService(intent);
		}
	}

	/**
	 * ��ע��㲥
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(playerReceiver);
		System.out.println("PlayerActivity has stoped");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("PlayerActivity has Destoryed");
	}

	/**
	 * �ؼ�����¼�
	 *
	 * @author wwj
	 *
	 */
	private class ViewOnclickListener implements OnClickListener {
		Intent intent = new Intent();

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.play_music:
				if (isPlaying) {
					playBtn.setBackgroundResource(R.drawable.pause_selector);
					intent.setAction("com.dieke.feldsher.MUSIC_SERVICE");
					intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
					startService(intent);
					isPlaying = false;
					isPause = true;
				} else if (isPause) {
					playBtn.setBackgroundResource(R.drawable.play_selector);
					intent.setAction("com.dieke.feldsher.MUSIC_SERVICE");
					intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
					startService(intent);
					isPause = false;
					isPlaying = true;
				}
				break;
			}
		}
	}

	/**
	 * ʵ�ּ���Seekbar����
	 *
	 * @author wwj
	 *
	 */
	private class SeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			switch(seekBar.getId()) {
			case R.id.audioTrack:
				if (fromUser) {
					audioTrackChange(progress); // �û����ƽ��ȵĸı�
				}
				break;
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

	}

	/**
	 * ��������
	 */
	public void play() {
		// ��ʼ���ŵ�ʱ��Ϊ˳�򲥷�
		repeat_none();
		Intent intent = new Intent();
		intent.setAction("com.dieke.feldsher.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("listPosition", listPosition);
		intent.putExtra("MSG", flag);
		startService(intent);
	}



	/**
	 * ���Ž��ȸı�
	 * @param progress
	 */
	public void audioTrackChange(int progress) {
		Intent intent = new Intent();
		intent.setAction("com.dieke.feldsher.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("listPosition", listPosition);
		intent.putExtra("MSG", AppConstant.PlayerMsg.PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);
	}

	/**
	 * ����ѭ��
	 */
	public void repeat_one() {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 1);
		sendBroadcast(intent);
	}

	/**
	 * ȫ��ѭ��
	 */
	public void repeat_all() {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 2);
		sendBroadcast(intent);
	}

	/**
	 * ˳�򲥷�
	 */
	public void repeat_none() {
		Intent intent = new Intent(CTL_ACTION);
		intent.putExtra("control", 3);
		sendBroadcast(intent);
	}





	public class PlayerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentTime = intent.getIntExtra("currentTime", -1);
				currentProgress.setText(MediaUtil.formatTime(currentTime));
				music_progressBar.setProgress(currentTime);
			} else if (action.equals(MUSIC_DURATION)) {
				int duration = intent.getIntExtra("duration", -1);
				music_progressBar.setMax(duration);
				finalProgress.setText(MediaUtil.formatTime(duration));
			} else if (action.equals(UPDATE_ACTION)) {
				listPosition = intent.getIntExtra("current", -1);
				url = mp3Infos.get(listPosition).getUrl();
				if (listPosition >= 0) {
//					mHeader.setTitle(mp3Infos.get(listPosition).getTitle());
				}
				if (listPosition == 0) {
					finalProgress.setText(MediaUtil.formatTime(mp3Infos.get(
							listPosition).getDuration()));
					playBtn.setBackgroundResource(R.drawable.pause_selector);
					isPause = true;
				}
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch(keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if(action == KeyEvent.ACTION_UP) {
				if(currentVolume < maxVolume) {
					currentVolume = currentVolume + 1;
					am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				} else {
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			}
			return false;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if(action == KeyEvent.ACTION_UP) {
				if(currentVolume > 0) {
					currentVolume = currentVolume - 1;
					am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				} else {
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			}
			return false;
			default:
				return super.dispatchKeyEvent(event);
		}
	}
}
