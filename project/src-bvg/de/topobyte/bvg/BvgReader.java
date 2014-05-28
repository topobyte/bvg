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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

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

		EncodingStrategy strategy;
		byte bStrategy = dis.readByte();
		switch (bStrategy) {
		default:
		case Constants.STRATEGY_DOUBLE:
			strategy = EncodingStrategy.STRATEGY_DOUBLE;
			break;
		case Constants.STRATEGY_INT_DELTA:
			strategy = EncodingStrategy.STRATEGY_INTEGER_DELTA;
			break;
		}

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

		BvgInputStream input;
		switch (strategy) {
		default:
		case STRATEGY_DOUBLE:
			input = new BvgInputStreamDouble(image, dis);
			break;
		case STRATEGY_INTEGER_DELTA:
			input = new BvgInputStreamIntegerDelta(image, dis);
			break;
		}

		input.read();
	}

}
