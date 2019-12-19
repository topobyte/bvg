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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import de.topobyte.bvg.BvgAndroidPainter;

public class BvgToggleButton extends BvgButton
{

	protected int colorFillToggled = 0x600000ff;

	private boolean status = false;

	public BvgToggleButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BvgToggleButton(Context context, float bw, float bh, float cr,
			boolean status)
	{
		super(context, bw, bh, cr);
		this.status = status;
	}

	public BvgToggleButton(Context context, float bw, float bh, float cr,
			String iconPath, float iconWidth, float iconHeight, boolean status)
	{
		super(context, bw, bh, cr, iconPath, iconWidth, iconHeight);
		this.status = status;
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (status) {
			fill.setColor(colorFillToggled);
		} else {
			fill.setColor(colorFill);
		}
		canvas.drawPath(path, fill);
		canvas.drawPath(path, stroke);
		if (icon != null) {
			BvgAndroidPainter.draw(canvas, icon, ix, iy, scaleW, scaleH,
					scaleW);
		}
	}

	public void toggle()
	{
		status = !status;
	}

	public boolean getStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		if (this.status == status) {
			return;
		}
		this.status = status;
		postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getActionMasked();
		if (action == MotionEvent.ACTION_UP) {
			toggle();
			postInvalidate();
			performClick();
		}
		return true;
	}

}
