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

import de.topobyte.bvg.compact.CompactWriter;
import de.topobyte.bvg.path.CubicTo;
import de.topobyte.bvg.path.LineTo;
import de.topobyte.bvg.path.MoveTo;
import de.topobyte.bvg.path.Path;
import de.topobyte.bvg.path.PathElement;
import de.topobyte.bvg.path.QuadTo;
import de.topobyte.bvg.path.Type;

public class BvgOutputStreamIntegerDelta extends AbstractBvgOutputStream
{

	private final CompactWriter writer;

	public BvgOutputStreamIntegerDelta(OutputStream os, EncodingMethod method,
			double width, double height) throws IOException
	{
		super(os, method, EncodingStrategy.STRATEGY_INTEGER_DELTA, width,
				height);
		writer = new CompactWriter(dos);
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

		writer.writeVariableLengthUnsignedInteger(elements.size());
		writer.writeVariableLengthUnsignedInteger(n);

		for (int i = 0; i < types.size(); i++) {
			Type type = types.get(i);
			switch (type) {
			case MOVE: {
				writer.writeByte(Constants.PATH_MOVE_TO);
				break;
			}
			case CLOSE: {
				writer.writeByte(Constants.PATH_CLOSE);
				break;
			}
			case LINE: {
				writer.writeByte(Constants.PATH_LINE_TO);
				break;
			}
			case QUAD: {
				writer.writeByte(Constants.PATH_QUAD_TO);
				break;
			}
			case CUBIC: {
				writer.writeByte(Constants.PATH_CUBIC_TO);
				break;
			}
			}
		}

		for (int i = 0; i < types.size(); i++) {
			Type type = types.get(i);
			PathElement pathElement = elements.get(i);

			switch (type) {
			case MOVE: {
				MoveTo move = (MoveTo) pathElement;
				int x = toIntegerX(move.getX());
				int y = toIntegerY(move.getY());
				writer.writeVariableLengthSignedInteger(x);
				writer.writeVariableLengthSignedInteger(y);
				break;
			}
			case CLOSE: {
				break;
			}
			case LINE: {
				LineTo line = (LineTo) pathElement;
				int x = toIntegerX(line.getX());
				int y = toIntegerY(line.getY());
				writer.writeVariableLengthSignedInteger(x);
				writer.writeVariableLengthSignedInteger(y);
				break;
			}
			case QUAD: {
				QuadTo quad = (QuadTo) pathElement;
				int cx = toIntegerX(quad.getCX());
				int cy = toIntegerY(quad.getCY());
				int x = toIntegerX(quad.getX());
				int y = toIntegerY(quad.getY());
				writer.writeVariableLengthSignedInteger(cx);
				writer.writeVariableLengthSignedInteger(cy);
				writer.writeVariableLengthSignedInteger(x);
				writer.writeVariableLengthSignedInteger(y);
				break;
			}
			case CUBIC: {
				CubicTo cubic = (CubicTo) pathElement;
				int cx = toIntegerX(cubic.getCX());
				int cy = toIntegerY(cubic.getCY());
				int cx2 = toIntegerX(cubic.getCX2());
				int cy2 = toIntegerY(cubic.getCY2());
				int x = toIntegerX(cubic.getX());
				int y = toIntegerY(cubic.getY());
				writer.writeVariableLengthSignedInteger(cx);
				writer.writeVariableLengthSignedInteger(cy);
				writer.writeVariableLengthSignedInteger(cx2);
				writer.writeVariableLengthSignedInteger(cy2);
				writer.writeVariableLengthSignedInteger(x);
				writer.writeVariableLengthSignedInteger(y);
				break;
			}
			}
		}
	}

	private int lx = 0;
	private int ly = 0;

	private int toIntegerX(double x)
	{
		int ix = (int) Math.round((x / width) * IntegerDelta.UNIVERSE);
		int dx = ix - lx;
		lx = ix;
		return dx;
	}

	private int toIntegerY(double y)
	{
		int iy = (int) Math.round((y / height) * IntegerDelta.UNIVERSE);
		int dy = iy - ly;
		ly = iy;
		return dy;
	}

}
