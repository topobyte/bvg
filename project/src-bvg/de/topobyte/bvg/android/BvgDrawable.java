// Copyright 2015 Sebastian Kuerten
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
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import de.topobyte.bvg.BvgAndroidPainter;
import de.topobyte.bvg.BvgAndroidPainterWithCache;
import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;

public class BvgDrawable extends Drawable
{

	protected BvgImage image = null;

	private final CacheMode mode;

	private BvgAndroidPainterWithCache cachePainter = null;

	private Bitmap bitmap = null;
	private int lastWidth = 0;
	private int lastHeight = 0;

	public BvgDrawable(Context context, String iconPath, CacheMode mode)
	{
		this.mode = mode;
		AssetManager assets = context.getAssets();
		try {
			InputStream is = assets.open(iconPath);
			image = BvgIO.read(is);
			is.close();
		} catch (IOException e) {
			Log.e("overlay", "error while loading image", e);
		}
	}

	private float w = -1;
	private float h = -1;

	@Override
	protected void onBoundsChange(Rect bounds)
	{
		Log.e("bvg", "onBoundsChange");
		int width = bounds.width();
		int height = bounds.height();
		float scaleW = width / (float) image.getWidth();
		float scaleH = height / (float) image.getHeight();
		float scale = Math.min(scaleW, scaleH);
		w = (float) (scale * image.getWidth());
		h = (float) (scale * image.getHeight());
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (image == null) {
			return;
		}

		Rect bounds = getBounds();
		int width = bounds.width();
		int height = bounds.height();
		float scaleW = width / (float) image.getWidth();
		float scaleH = height / (float) image.getHeight();
		float scale = Math.min(scaleW, scaleH);
		float w = (float) (scale * image.getWidth());
		float h = (float) (scale * image.getHeight());

		Log.e("bvg", "canvas: " + width + " x " + height);
		Log.e("bvg", "image: " + image.getWidth() + " x " + image.getHeight());
		Log.e("bvg", "scaleH: " + scaleH);
		Log.e("bvg", "scaleW: " + scaleW);
		Log.e("bvg", "scale: " + scale);
		Log.e("bvg", "result: " + w + " x " + h);
		Log.e("bvg", "left, top: " + bounds.left + ", " + bounds.top);

		canvas.save();
		canvas.clipRect(0, 0, w, h);
		if (mode == CacheMode.NOTHING) {
			BvgAndroidPainter.draw(canvas, image, 0, 0, scale, scale, scale);
		} else if (mode == CacheMode.PATHS) {
			if (cachePainter == null) {
				cachePainter = new BvgAndroidPainterWithCache(image);
			}
			cachePainter.draw(canvas, 0, 0, scale, scale, scale);
		} else if (mode == CacheMode.OFFSCREEN) {
			boolean newImage = true;
			if (bitmap != null) {
				newImage = width != lastWidth || height != lastHeight;
			}
			if (newImage) {
				if (bitmap != null) {
					bitmap.recycle();
				}
				bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
				lastWidth = width;
				lastHeight = height;
				Canvas c = new Canvas(bitmap);
				BvgAndroidPainter.draw(c, image, 0, 0, scale, scale, scale);
			}
			canvas.drawBitmap(bitmap, 0, 0, new Paint());
		}
		canvas.restore();
	}

	@Override
	public void setAlpha(int alpha)
	{
		// do nothing?
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		// do nothing?
	}

	@Override
	public int getOpacity()
	{
		return PixelFormat.TRANSLUCENT;
	}

	/*
	 * We do NOT return an intrinsic size different from (-1,-1) because
	 * ImageView will then render the Drawable at this size and perform the
	 * scaling itself. Not what we want for vector graphics!
	 */

	@Override
	public int getIntrinsicHeight()
	{
		Log.e("bvg", "getIntrinsicHeight: " + h);
		// return Math.round((float) (image.getHeight()));
		return super.getIntrinsicHeight();
	}

	@Override
	public int getIntrinsicWidth()
	{
		Log.e("bvg", "getIntrinsicWidth: " + w);
		// return Math.round((float) (image.getWidth()));
		return super.getIntrinsicWidth();
	}

}
