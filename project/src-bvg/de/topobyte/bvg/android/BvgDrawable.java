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
import android.view.Gravity;
import de.topobyte.bvg.BvgAndroidPainter;
import de.topobyte.bvg.BvgAndroidPainterWithCache;
import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;

public class BvgDrawable extends Drawable
{

	protected BvgImage image = null;

	private final CacheMode mode;
	private final int gravity;

	private BvgAndroidPainterWithCache cachePainter = null;

	private Bitmap bitmap = null;
	private int lastWidth = 0;
	private int lastHeight = 0;

	public BvgDrawable(Context context, String iconPath, CacheMode mode)
	{
		this(context, iconPath, mode, Gravity.NO_GRAVITY);
	}

	public BvgDrawable(Context context, String iconPath, CacheMode mode,
			int gravity)
	{
		this.mode = mode;
		this.gravity = gravity;
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

		// Determine the maximal size that fits into the bounds
		Rect bounds = getBounds();
		int width = bounds.width();
		int height = bounds.height();
		float scaleW = width / (float) image.getWidth();
		float scaleH = height / (float) image.getHeight();
		float scale = Math.min(scaleW, scaleH);
		float w = (float) (scale * image.getWidth());
		float h = (float) (scale * image.getHeight());

		// Apply the gravity using the size computed before
		Rect target = new Rect();
		int iw = Math.round(w);
		int ih = Math.round(h);
		Gravity.apply(gravity, iw, ih, bounds, target);

		// Determine new size and scale values depending on the results that
		// resulted from applying the gravity
		int tx = target.left;
		int ty = target.top;
		int tw = target.width();
		int th = target.height();
		float tscaleX = tw / (float) image.getWidth();
		float tscaleY = th / (float) image.getHeight();
		float tscaleW = Math.min(tscaleX, tscaleY);

		// These are the scale values that we will use for drawing
		float sx = tscaleX;
		float sy = tscaleY;
		float sw = tscaleW;

		Log.i("bvg", "canvas: " + width + " x " + height);
		Log.i("bvg", "image: " + image.getWidth() + " x " + image.getHeight());
		Log.i("bvg", "scaleH: " + scaleH);
		Log.i("bvg", "scaleW: " + scaleW);
		Log.i("bvg", "scale: " + scale);
		Log.i("bvg", "result: " + w + " x " + h);
		Log.i("bvg", "left, top: " + bounds.left + ", " + bounds.top);

		Log.i("bvg", "target: " + target);
		Log.i("bvg", "sx: " + sx);
		Log.i("bvg", "sy: " + sy);
		Log.i("bvg", "sw: " + sw);

		canvas.save();
		canvas.clipRect(target.left, target.top, target.right, target.bottom);
		if (mode == CacheMode.NOTHING) {
			BvgAndroidPainter.draw(canvas, image, tx, ty, sx, sy, sw);
		} else if (mode == CacheMode.PATHS) {
			if (cachePainter == null) {
				cachePainter = new BvgAndroidPainterWithCache(image);
			}
			cachePainter.draw(canvas, tx, ty, sx, sy, sw);
		} else if (mode == CacheMode.OFFSCREEN) {
			boolean newImage = true;
			if (bitmap != null) {
				newImage = tw != lastWidth || th != lastHeight;
			}
			if (newImage) {
				if (bitmap != null) {
					bitmap.recycle();
				}
				bitmap = Bitmap.createBitmap(tw, th, Config.ARGB_8888);
				lastWidth = tw;
				lastHeight = th;
				Canvas c = new Canvas(bitmap);
				BvgAndroidPainter.draw(c, image, 0, 0, sx, sy, sw);
			}
			canvas.drawBitmap(bitmap, tx, ty, new Paint());
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
		Log.i("bvg", "getIntrinsicHeight: " + h);
		// return Math.round((float) (image.getHeight()));
		return super.getIntrinsicHeight();
	}

	@Override
	public int getIntrinsicWidth()
	{
		Log.i("bvg", "getIntrinsicWidth: " + w);
		// return Math.round((float) (image.getWidth()));
		return super.getIntrinsicWidth();
	}

}
