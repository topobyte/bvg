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
import java.io.OutputStream;

import de.topobyte.bvg.BvgOutputStream;
import de.topobyte.bvg.BvgOutputStreamDouble;
import de.topobyte.bvg.BvgOutputStreamIntegerDelta;
import de.topobyte.bvg.Color;
import de.topobyte.bvg.EncodingMethod;
import de.topobyte.bvg.EncodingStrategy;
import de.topobyte.bvg.Fill;
import de.topobyte.bvg.LineStyle;
import de.topobyte.bvg.Stroke;
import de.topobyte.bvg.path.Path;

public class SvgToBvg implements ShapeSink
{

	private BvgOutputStream bvgOutputStream;
	private final OutputStream os;

	private final EncodingMethod method;
	private final EncodingStrategy strategy;

	public SvgToBvg(OutputStream os, EncodingMethod method,
			EncodingStrategy strategy) throws IOException
	{
		this.os = os;
		this.method = method;
		this.strategy = strategy;
	}

	@Override
	public void init(double width, double height) throws IOException
	{
		switch (strategy) {
		default:
		case STRATEGY_DOUBLE:
			bvgOutputStream = new BvgOutputStreamDouble(os, method, width,
					height);
			break;
		case STRATEGY_INTEGER_DELTA:
			bvgOutputStream = new BvgOutputStreamIntegerDelta(os, method, width,
					height);
			break;
		}
	}

	@Override
	public void finish() throws IOException
	{
		bvgOutputStream.close();
	}

	@Override
	public void fill(Shape shape, Color color) throws IOException
	{
		Path path = FromSwingUtil.createPath(shape);

		Fill fill = new Fill(color);
		bvgOutputStream.fill(fill, path);
	}

	@Override
	public void stroke(Shape shape, Color color, LineStyle lineStyle)
			throws IOException
	{
		Path path = FromSwingUtil.createPath(shape);

		Stroke stroke = new Stroke(color, lineStyle);
		bvgOutputStream.stroke(stroke, path);
	}

}