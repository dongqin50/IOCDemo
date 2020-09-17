package com.conagra.hardware.model;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * �Զ���滭��ʣ���������Ч��
 * @author wwj
 *
 */
public class LrcView extends TextView {
	private float width;		//�����ͼ���
	private float height;		//�����ͼ�߶�
	private Paint currentPaint;	//��ǰ���ʶ���
	private Paint notCurrentPaint;	//�ǵ�ǰ���ʶ���
	private float textHeight = 25;	//�ı��߶�
	private float textSize = 18;		//�ı���С
	private int index = 0;		//list�����±�
	
	
	private List<LrcContent> mLrcList = new ArrayList<LrcContent>();
	
	public void setmLrcList(List<LrcContent> mLrcList) {
		this.mLrcList = mLrcList;
	}

	public LrcView(Context context) {
		super(context);
		init();
	}
	public LrcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setFocusable(true);		//���ÿɶԽ�
		
		//��������
		currentPaint = new Paint();
		currentPaint.setAntiAlias(true);	//���ÿ���ݣ����������۱���
		currentPaint.setTextAlign(Paint.Align.CENTER);//�����ı����뷽ʽ
		
		//�Ǹ�������
		notCurrentPaint = new Paint();
		notCurrentPaint.setAntiAlias(true);
		notCurrentPaint.setTextAlign(Paint.Align.CENTER);
	}
	
	/**
	 * �滭���
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(canvas == null) {
			return;
		}
		
		currentPaint.setColor(Color.argb(210, 251, 248, 29));
		notCurrentPaint.setColor(Color.argb(140, 255, 255, 255));
		
		currentPaint.setTextSize(24);
		currentPaint.setTypeface(Typeface.SERIF);
		
		notCurrentPaint.setTextSize(textSize);
		notCurrentPaint.setTypeface(Typeface.DEFAULT);
		
		try {
			setText("");
			canvas.drawText(mLrcList.get(index).getLrcStr(), width / 2, height / 2, currentPaint);
			
			float tempY = height / 2;
			//��������֮ǰ�ľ���
			for(int i = index - 1; i >= 0; i--) {
				//��������
				tempY = tempY - textHeight;
				canvas.drawText(mLrcList.get(i).getLrcStr(), width / 2, tempY, notCurrentPaint);
			}
			tempY = height / 2;
			//��������֮��ľ���
			for(int i = index + 1; i < mLrcList.size(); i++) {
				//��������
				tempY = tempY + textHeight;
				canvas.drawText(mLrcList.get(i).getLrcStr(), width / 2, tempY, notCurrentPaint);
			} 
		} catch (Exception e) {
			setText("...ľ�и���ļ����Ͻ�ȥ����...");
		}
	}

	/**
	 * ��view��С�ı��ʱ����õķ���
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.width = w;
		this.height = h;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
