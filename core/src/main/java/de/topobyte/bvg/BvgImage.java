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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.topobyte.bvg.path.CompactPath;

public class BvgImage
{

	private double width;
	private double height;

	private List<PaintElement> elements = new ArrayList<PaintElement>();
	private List<CompactPath> paths = new ArrayList<CompactPath>();

	public BvgImage(double width, double height)
	{
		this.width = width;
		this.height = height;
	}

	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}

	public void addFill(Fill fill, CompactPath path)
	{
		elements.add(fill);
		paths.add(path);
	}

	public void addStroke(Stroke stroke, CompactPath path)
	{
		elements.add(stroke);
		paths.add(path);
	}

	public List<PaintElement> getPaintElements()
	{
		return Collections.unmodifiableList(elements);
	}

	public List<CompactPath> getPaths()
	{
		return Collections.unmodifiableList(paths);
	}

}
