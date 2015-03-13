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
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import de.topobyte.bvg.BvgAndroidPainter;
import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;

public class BvgDrawable extends Drawable
{

	protected float bw;
	protected float bh;

	protected BvgImage image = null;

	public BvgDrawable(Context context, String iconPath)
	{
		AssetManager assets = context.getAssets();
		try {
			InputStream is = assets.open(iconPath);
			image = BvgIO.read(is);
			is.close();
		} catch (IOException e) {
			Log.e("overlay", "error while loading image", e);
		}
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (image == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		float scaleW = (float) (width / image.getWidth());
		float scaleH = (float) (height / image.getHeight());
		float scale = Math.min(scaleW, scaleH);
		BvgAndroidPainter.draw(canvas, image, 0, 0, scale, scale, scale);
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
		return PixelFormat.OPAQUE;
	}

}
