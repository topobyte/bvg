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

public class Color implements IColor
{
	private int argb;

	public Color(int argb)
	{
		this.argb = setFullAlpha(argb);
	}

	public Color(int rgb, int alpha)
	{
		argb = (rgb & 0xFFFFFF) | ((alpha & 0xFF) << 24);
	}

	public Color(int argb, boolean hasAlpha)
	{
		if (hasAlpha) {
			this.argb = argb;
		} else {
			this.argb = setFullAlpha(argb);
		}
	}

	public Color(int r, int g, int b)
	{
		this(r, g, b, 255);
	}

	public Color(int r, int g, int b, int a)
	{
		argb = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8)
				| ((b & 0xFF) << 0);
	}

	private int setFullAlpha(int rgb)
	{
		return 0xff000000 | rgb;
	}

	@Override
	public int getColorCode()
	{
		return getARGB();
	}

	public int getARGB()
	{
		return argb;
	}

	public int getRGB()
	{
		return argb & 0xFFFFFF;
	}

	@Override
	public int getRed()
	{
		return (argb & 0xff0000) >>> 16;
	}

	@Override
	public int getGreen()
	{
		return (argb & 0xff00) >>> 8;
	}

	@Override
	public int getBlue()
	{
		return argb & 0xff;
	}

	@Override
	public int getAlpha()
	{
		int a = (argb & 0xff000000) >>> 24;
		return a;
	}

}
