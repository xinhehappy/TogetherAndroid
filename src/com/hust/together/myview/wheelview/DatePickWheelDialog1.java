/*
 *  Copyright 2012 by Handsomedylan
 *	ʹ��AlertDialog��˼·�����˷�װ��,ͬʱ�Ժ��ʵ���������Ӧ������Ļ��
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.hust.together.myview.wheelview;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hust.together.ui.R;

public class DatePickWheelDialog1 extends Dialog {

	private static int START_YEAR = 1990, END_YEAR = 2100;
	private static WheelView wv_year;
	private static WheelView wv_month;
	private static WheelView wv_day;
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };

	final List<String> list_big = Arrays.asList(months_big);
	final List<String> list_little = Arrays.asList(months_little);
	private final Context mContext;
	private Button btn_sure;
	private CharSequence positiveText;
	private Button btn_cancel;
	private CharSequence negativeText;
	private Calendar calendar;
	private View.OnClickListener positiveClickListener;
	private View.OnClickListener negativeClickListener;

	private DatePickWheelDialog1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	private DatePickWheelDialog1(Context context, Calendar instance) {
		// TODO Auto-generated constructor stub
		this(context);
		calendar = instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.time_layout1);

		findView();
		adjustView();
		setListener();
		setDate(calendar);

	}

	private void adjustView() {
		// TODO Auto-generated method stub
		// ������Ļ�ܶ���ָ��ѡ��������Ĵ�С
		int textSize = 0;

		textSize = pixelsToDip(mContext.getResources(), 13);

		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
	}

	public static int pixelsToDip(Resources res, int pixels) {
		final float scale = res.getDisplayMetrics().density;
		return (int) (pixels * scale + 0.5f);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		// ȡ��
		if (negativeClickListener != null) {
			btn_cancel.setOnClickListener(negativeClickListener);
		} else {
			btn_cancel.setOnClickListener(dismissListener);
		}
		if (positiveClickListener != null) {
			btn_sure.setOnClickListener(positiveClickListener);
		} else {
			btn_sure.setOnClickListener(dismissListener);
		}

	}

	private final View.OnClickListener dismissListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dismiss();
		}
	};

	private void findView() {
		// TODO Auto-generated method stub
		// ��
		wv_year = (WheelView) findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// ����"��"����ʾ����
		wv_year.setLabel("��");// �������

		// ��
		wv_month = (WheelView) findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setLabel("��");

		// ��
		wv_day = (WheelView) findViewById(R.id.day);
		// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
		wv_day.setLabel("��");

		// ʱ

		btn_sure = (Button) findViewById(R.id.btn_datetime_sure);
		if (positiveText != null) {
			btn_sure.setVisibility(View.VISIBLE);
			btn_sure.setText(positiveText);
		}
		btn_cancel = (Button) findViewById(R.id.btn_datetime_cancel);
		if (negativeText != null) {
			btn_cancel.setVisibility(View.VISIBLE);
			btn_cancel.setText(negativeText);
		}
	}

	// ���"��"����
	private final OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			int year_num = newValue + START_YEAR;
			// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
			if (list_big
					.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(wv_month
					.getCurrentItem() + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				if ((year_num % 4 == 0 && year_num % 100 != 0)
						|| year_num % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
		}
	};
	// ���"��"����
	private final OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			int month_num = newValue + 1;
			// �жϴ�С�¼��Ƿ�����,����ȷ��"��"������
			if (list_big.contains(String.valueOf(month_num))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month_num))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
						.getCurrentItem() + START_YEAR) % 100 != 0)
						|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
		}
	};

	private void setPositiveButton(CharSequence mPositiveButtonText,
			View.OnClickListener onClickListener) {
		positiveText = mPositiveButtonText;
		positiveClickListener = onClickListener;// can't use btn_sure here
												// because it's on defined yet
	}

	private void setNegativeButton(CharSequence mNegativeButtonText,
			View.OnClickListener onClickListener) {
		negativeText = mNegativeButtonText;
		negativeClickListener = onClickListener;// can't use btn_sure here
												// because it's on defined yet
	}

	private void setCalendar(Calendar calendar) {
		// TODO Auto-generated method stub
		this.calendar = calendar;
	}

	public DatePickWheelDialog1 setDate(Calendar calendar) {
		if (calendar == null)
			return this;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);

		wv_year.setCurrentItem(year - START_YEAR);// ��ʼ��ʱ��ʾ������
		wv_month.setCurrentItem(month);
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// ����
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setCurrentItem(day);
		return this;
	}

	public static Calendar getSetCalendar() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.set(wv_year.getCurrentItem() + START_YEAR, wv_month.getCurrentItem(),
				wv_day.getCurrentItem() + 1);
		return c;
	}

	public static class Builder {
		private final DatePickParams P;

		public Builder(Context context) {
			P = new DatePickParams(context);
		}

		public Builder setTitle(CharSequence title) {
			P.mTitle = title;
			return this;
		}

		public Builder setIcon(int iconId) {
			P.mIconId = iconId;
			return this;
		}

		public Builder setPositiveButton(CharSequence text,
				final View.OnClickListener listener) {
			P.mPositiveButtonText = text;
			P.mPositiveButtonListener = listener;
			return this;
		}

		public Builder setNegativeButton(CharSequence text,
				final View.OnClickListener listener) {
			// TODO Auto-generated method stub
			P.mNegativeButtonText = text;
			P.mNegativeButtonListener = listener;
			return this;
		}

		public DatePickWheelDialog1 create() {
			final DatePickWheelDialog1 dialog = new DatePickWheelDialog1(
					P.mContext);
			P.apply(dialog);
			return dialog;
		}
	}

	public static class DatePickParams {
		public int mIconId;
		public View.OnClickListener mPositiveButtonListener;
		public CharSequence mPositiveButtonText;
		public CharSequence mTitle;
		public final Context mContext;
		public Calendar calendar;
		private CharSequence mNegativeButtonText;
		private View.OnClickListener mNegativeButtonListener;

		public DatePickParams(Context context) {
			mContext = context;
			calendar = Calendar.getInstance();
		};

		public DatePickParams(Context context, Calendar calendar) {
			mContext = context;
			this.calendar = calendar;
		}

		public void apply(DatePickWheelDialog1 dialog) {
			if (mTitle != null) {
				dialog.setTitle(mTitle);
			}

			if (mPositiveButtonText != null) {
				dialog.setPositiveButton(mPositiveButtonText,
						mPositiveButtonListener);
			}
			if (mNegativeButtonText != null) {
				dialog.setNegativeButton(mNegativeButtonText,
						mNegativeButtonListener);
			}
			if (calendar != null)
				dialog.setCalendar(calendar);

		}
	}

}
