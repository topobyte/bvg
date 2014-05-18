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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

import de.topobyte.bvg.path.CompactPath;
import de.topobyte.bvg.path.Type;

public class BvgReader
{

	public static BvgImage read(File file) throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream is = new BufferedInputStream(fis);
		BvgImage image = read(is);
		is.close();
		return image;
	}

	public static BvgImage read(InputStream is) throws IOException
	{
		BvgReader reader = new BvgReader(is);
		reader.read();
		return reader.image;
	}

	private BvgImage image = null;
	private InputStream is;
	private DataInputStream dis;

	private BvgReader(InputStream is)
	{
		this.is = is;
		dis = new DataInputStream(is);
	}

	private void read() throws IOException
	{
		byte[] magic = new byte[4];
		dis.readFully(magic, 0, magic.length);
		short version = dis.readShort();
		byte encoding = dis.readByte();
		// System.out.println("Version: " + version);
		// System.out.println("Encoding: " + encoding);

		double width = dis.readDouble();
		double height = dis.readDouble();
		image = new BvgImage(width, height);

		// System.out.println("Size: " + width + ", " + height);

		if (encoding == Constants.ENCODING_PLAIN) {
			// valid encoding
		} else if (encoding == Constants.ENCODING_DEFLATE) {
			InflaterInputStream deflater = new InflaterInputStream(is);
			dis = new DataInputStream(deflater);
		} else {
			throw new IOException("Illegal encoding");
		}

		while (true) {
			byte head;
			try {
				head = dis.readByte();
			} catch (EOFException e) {
				break;
			}
			if (head == Constants.ID_FILL) {
				readFill();
			} else if (head == Constants.ID_STROKE) {
				readStroke();
			}
		}

		dis.close();
	}

	private void readFill() throws IOException
	{
		// System.out.println("Fill");
		int colorCode = dis.readInt();

		Color color = new Color(colorCode, true);

		// System.out.println(String.format("Color: 0x%08X", colorCode));

		CompactPath path = readPath();

		image.addFill(new Fill(color), path);
	}

	private void readStroke() throws IOException
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

		LineStyle lineStyle = new LineStyle(lineWidth, cap, join);

		if (join == Join.MITER) {
			float miterLimit = dis.readFloat();
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
