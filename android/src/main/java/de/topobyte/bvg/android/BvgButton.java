// Copyright 2014 Sebastian Kuerten
//
// This file is part of bvg.
//
// bvg is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// bvg is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with bvg. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.bvg.android;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import de.topobyte.bvg.BvgAndroidPainter;
import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;

public class BvgButton extends View
{

	protected int colorFill = 0x99666666;
	protected int colorStroke = 0xAA333333;

	protected Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);

	protected Path path;

	protected float bw;
	protected float bh;
	protected float cr;

	protected BvgImage icon = null;
	protected float scaleW = 0;
	protected float scaleH = 0;
	protected float iconWidth = 0;
	protected float iconHeight = 0;
	protected float ix = 0;
	protected float iy = 0;

	protected float strokeWidth;

	public BvgButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BvgButton(Context context, float bw, float bh, float cr)
	{
		super(context);
		this.bw = bw;
		this.bh = bh;
		this.cr = cr;
		path = new Path();
		init();
	}

	public BvgButton(Context context, float bw, float bh, float cr,
			String iconPath, float iconWidth, float iconHeight)
	{
		super(context);
		this.bw = bw;
		this.bh = bh;
		this.cr = cr;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
		path = new Path();
		init();

		AssetManager assets = context.getAssets();
		try {
			InputStream is = assets.open(iconPath);
			icon = BvgIO.read(is);
			scaleW = (float) (iconWidth / icon.getWidth());
			scaleH = (float) (iconHeight / icon.getHeight());
			is.close();
		} catch (IOException e) {
			Log.e("overlay", "error while loading icon", e);
		}
	}

	private void init()
	{
		float density = getResources().getDisplayMetrics().density;
		strokeWidth = 1 * density;
		initPaints(density);
	}

	private void initPaints(float density)
	{
		fill.setStyle(Paint.Style.FILL);
		fill.setColor(colorFill);
		stroke.setStyle(Paint.Style.STROKE);
		stroke.setColor(colorStroke);
		stroke.setStrokeWidth(strokeWidth);
	}

	protected void initPath(int width, int height)
	{
		float x1 = strokeWidth / 2;
		float y1 = strokeWidth / 2;
		float x2 = x1 + width - strokeWidth;
		float y2 = y1 + height - strokeWidth;

		path.rewind();
		path.moveTo(x1 + cr, y1);
		path.lineTo(x2 - cr, y1);
		path.quadTo(x2, y1, x2, y1 + cr);
		path.lineTo(x2, y2 - cr);
		path.quadTo(x2, y2, x2 - cr, y2);
		path.lineTo(x1 + cr, y2);
		path.quadTo(x1, y2, x1, y2 - cr);
		path.lineTo(x1, y1 + cr);
		path.quadTo(x1, y1, x1 + cr, y1);
		path.close();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawPath(path, fill);
		canvas.drawPath(path, stroke);
		if (icon != null) {
			BvgAndroidPainter.draw(canvas, icon, ix, iy, scaleW, scaleH,
					scaleW);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		initPath(w, h);

		float x1 = strokeWidth / 2;
		float y1 = strokeWidth / 2;
		ix = x1 + (bw - iconWidth) / 2;
		iy = y1 + (bh - iconHeight) / 2;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int desiredWidth = Math.round(bw);
		int desiredHeight = Math.round(bh);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(desiredWidth, widthSize);
		} else {
			width = desiredWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(desiredHeight, heightSize);
		} else {
			height = desiredHeight;
		}

		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getActionMasked();
		if (action == MotionEvent.ACTION_UP) {
			performClick();
		}
		return true;
	}

}
