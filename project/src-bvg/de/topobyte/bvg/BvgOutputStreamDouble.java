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

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import de.topobyte.bvg.path.CubicTo;
import de.topobyte.bvg.path.LineTo;
import de.topobyte.bvg.path.MoveTo;
import de.topobyte.bvg.path.Path;
import de.topobyte.bvg.path.PathElement;
import de.topobyte.bvg.path.QuadTo;
import de.topobyte.bvg.path.Type;

public class BvgOutputStreamDouble extends AbstractBvgOutputStream
{

	public BvgOutputStreamDouble(OutputStream os, boolean compress,
			double width, double height) throws IOException
	{
		super(os, compress, EncodingStrategy.STRATEGY_DOUBLE, width, height);
	}

	@Override
	public void fill(Fill fill, Path path) throws IOException
	{
		super.writeFillCode(path);
		IColor color = fill.getColor();
		int code = color.getColorCode();
		dos.writeInt(code);
		write(path);
	}

	@Override
	public void stroke(Stroke stroke, Path path) throws IOException
	{
		dos.writeByte(Constants.ID_STROKE);
		IColor color = stroke.getColor();
		int code = color.getColorCode();
		dos.writeInt(code);
		LineStyle lineStyle = stroke.getLineStyle();
		float width = lineStyle.getWidth();
		dos.writeFloat(width);
		Cap cap = lineStyle.getCap();
		switch (cap) {
		case BUTT:
			dos.writeByte(Constants.CAP_BUTT);
			break;
		case ROUND:
			dos.writeByte(Constants.CAP_ROUND);
			break;
		case SQUARE:
			dos.writeByte(Constants.CAP_SQUARE);
			break;
		}
		Join join = lineStyle.getJoin();
		switch (join) {
		case BEVEL:
			dos.writeByte(Constants.JOIN_BEVEL);
			break;
		case MITER:
			dos.writeByte(Constants.JOIN_MITER);
			break;
		case ROUND:
			dos.writeByte(Constants.JOIN_ROUND);
			break;
		}
		if (join == Join.MITER) {
			float miterLimit = lineStyle.getMiterLimit();
			dos.writeFloat(miterLimit);
		}
		float[] array = lineStyle.getDashArray();
		dos.writeBoolean(array != null);
		if (array != null) {
			dos.writeShort(array.length);
			for (int i = 0; i < array.length; i++) {
				dos.writeFloat(array[i]);
			}
			dos.writeFloat(lineStyle.getDashOffset());
		}
		write(path);
	}

	@Override
	public void write(Path path) throws IOException
	{
		List<Type> types = path.getTypes();
		List<PathElement> elements = path.getElements();

		int n = 0;
		for (int i = 0; i < types.size(); i++) {
			Type type = types.get(i);

			switch (type) {
			default:
			case CLOSE:
				break;
			case MOVE:
			case LINE:
				n += 2;
				break;
			case QUAD:
				n += 4;
				break;
			case CUBIC:
				n += 6;
				break;
			}
		}

		dos.writeInt(elements.size());
		dos.writeInt(n);

		for (int i = 0; i < types.size(); i++) {
			Type type = types.get(i);
			PathElement pathElement = elements.get(i);

			switch (type) {
			case MOVE:
				dos.writeByte(Constants.PATH_MOVE_TO);
				MoveTo move = (MoveTo) pathElement;
				dos.writeDouble(move.getX());
				dos.writeDouble(move.getY());
				break;
			case CLOSE:
				dos.writeByte(Constants.PATH_CLOSE);
				break;
			case LINE:
				dos.writeByte(Constants.PATH_LINE_TO);
				LineTo line = (LineTo) pathElement;
				dos.writeDouble(line.getX());
				dos.writeDouble(line.getY());
				break;
			case QUAD:
				dos.writeByte(Constants.PATH_QUAD_TO);
				QuadTo quad = (QuadTo) pathElement;
				dos.writeDouble(quad.getCX());
				dos.writeDouble(quad.getCY());
				dos.writeDouble(quad.getX());
				dos.writeDouble(quad.getY());
				break;
			case CUBIC:
				dos.writeByte(Constants.PATH_CUBIC_TO);
				CubicTo cubic = (CubicTo) pathElement;
				dos.writeDouble(cubic.getCX());
				dos.writeDouble(cubic.getCY());
				dos.writeDouble(cubic.getCX2());
				dos.writeDouble(cubic.getCY2());
				dos.writeDouble(cubic.getX());
				dos.writeDouble(cubic.getY());
				break;
			}
		}
	}
}
