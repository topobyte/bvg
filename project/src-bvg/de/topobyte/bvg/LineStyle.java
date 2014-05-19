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

package de.topobyte.bvg;

public class LineStyle
{

	private float width;
	private Cap cap;
	private Join join;
	private float miterLimit = 0;
	private float[] dashArray = null;
	private float dashOffset = 0;

	public LineStyle(float width, Cap cap, Join join)
	{
		this.width = width;
		this.cap = cap;
		this.join = join;
	}

	public LineStyle(float width, Cap cap, Join join, float[] dashArray,
			float dashOffset)
	{
		this(width, cap, join);
		this.dashArray = dashArray;
		this.dashOffset = dashOffset;
	}

	public void setMiterLimit(float miterLimit)
	{
		this.miterLimit = miterLimit;
	}

	public float getWidth()
	{
		return width;
	}

	public Cap getCap()
	{
		return cap;
	}

	public Join getJoin()
	{
		return join;
	}

	public float getMiterLimit()
	{
		return miterLimit;
	}

	public float[] getDashArray()
	{
		return dashArray;
	}

	public float getDashOffset()
	{
		return dashOffset;
	}
}
