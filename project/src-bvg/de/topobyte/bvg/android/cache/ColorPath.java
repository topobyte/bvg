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

package de.topobyte.bvg.android.cache;

import android.graphics.Path;

public class ColorPath
{

	private final Path path;
	private final int color;

	public ColorPath(Path path, int color)
	{
		this.path = path;
		this.color = color;
	}

	public Path getPath()
	{
		return path;
	}

	public int getColor()
	{
		return color;
	}

}
