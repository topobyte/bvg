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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.topobyte.bvg.path.CompactPath;
import de.topobyte.bvg.path.Type;

public class BvgInputStreamDouble extends AbstractBvgInputStream
{

	public BvgInputStreamDouble(BvgImage image, DataInputStream dis)
	{
		super(image, dis);
	}

	@Override
	public void readFill() throws IOException
	{
		// System.out.println("Fill");
		int colorCode = dis.readInt();

		Color color = new Color(colorCode, true);

		// System.out.println(String.format("Color: 0x%08X", colorCode));

		CompactPath path = readPath();

		image.addFill(new Fill(color), path);
	}

	@Override
	public void readStroke() throws IOException
	{
		// System.out.println("Stroke");
		int colorCode = dis.readInt();
		float lineWidth = dis.readFloat();
		byte bCap = dis.readByte();
		byte bJoin = dis.readByte();

		Color color = new Color(colorCode, true);

		Cap cap = null;
		switch (bCap) {
		case Constants.CAP_BUTT:
			cap = Cap.BUTT;
			break;
		case Constants.CAP_SQUARE:
			cap = Cap.SQUARE;
			break;
		case Constants.CAP_ROUND:
			cap = Cap.ROUND;
			break;
		}

		Join join = null;
		switch (bJoin) {
		case Constants.JOIN_BEVEL:
			join = Join.BEVEL;
			break;
		case Constants.JOIN_MITER:
			join = Join.MITER;
			break;
		case Constants.JOIN_ROUND:
			join = Join.ROUND;
			break;
		}

		float miterLimit = 0;
		if (join == Join.MITER) {
			miterLimit = dis.readFloat();
		}

		boolean hasDashArray = dis.readBoolean();
		float[] dashArray = null;
		float dashOffset = 0;
		if (hasDashArray) {
			int length = dis.readShort();
			dashArray = new float[length];
			for (int i = 0; i < length; i++) {
				dashArray[i] = dis.readFloat();
			}
			dashOffset = dis.readFloat();
		}

		LineStyle lineStyle;
		if (!hasDashArray) {
			lineStyle = new LineStyle(lineWidth, cap, join);
		} else {
			lineStyle = new LineStyle(lineWidth, cap, join, dashArray,
					dashOffset);
		}

		if (join == Join.MITER) {
			lineStyle.setMiterLimit(miterLimit);
		}

		// System.out.println(String.format("Color: 0x%08X", colorCode));
		// System.out.println("Line width: " + lineWidth);

		CompactPath path = readPath();

		image.addStroke(new Stroke(color, lineStyle), path);
	}

	private CompactPath readPath() throws IOException
	{
		int n = dis.readInt();
		int v = dis.readInt();

		List<Type> types = new ArrayList<Type>(n);
		double[] values = new double[v];

		int k = 0;
		for (int i = 0; i < n; i++) {
			byte t = dis.readByte();
			Type type = null;
			switch (t) {
			case Constants.PATH_CLOSE:
				type = Type.CLOSE;
				break;
			case Constants.PATH_MOVE_TO:
				type = Type.MOVE;
				break;
			case Constants.PATH_LINE_TO:
				type = Type.LINE;
				break;
			case Constants.PATH_QUAD_TO:
				type = Type.QUAD;
				break;
			case Constants.PATH_CUBIC_TO:
				type = Type.CUBIC;
				break;
			default:
				throw new IOException("Unexpected path element");
			}
			types.add(type);
			switch (type) {
			case CLOSE:
				break;
			case MOVE: {
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				break;
			}
			case LINE: {
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				break;
			}
			case QUAD: {
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				break;
			}
			case CUBIC: {
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				values[k++] = dis.readDouble();
				break;
			}
			}
		}
		return new CompactPath(types, values);
	}
}
