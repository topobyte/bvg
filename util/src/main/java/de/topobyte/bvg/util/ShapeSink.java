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

package de.topobyte.bvg.util;

import java.awt.Shape;
import java.io.IOException;

import de.topobyte.bvg.Color;
import de.topobyte.bvg.LineStyle;

public interface ShapeSink
{

	public void init(double width, double height) throws IOException;

	public void fill(Shape shape, Color color) throws IOException;

	public void stroke(Shape shape, Color color, LineStyle lineStyle)
			throws IOException;

	public void finish() throws IOException;

}
