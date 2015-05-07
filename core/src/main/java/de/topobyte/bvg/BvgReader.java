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
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class BvgReader
{

	BvgMetadata metadata = null;
	BvgImage image = null;

	private final InputStream is;
	private DataInputStream dis;

	BvgReader(InputStream is)
	{
		this.is = is;
		dis = new DataInputStream(is);
	}

	void read(BvgMetadata meta) throws IOException
	{
		if (meta != null) {
			metadata = meta;
		} else {
			metadata = new BvgMetadata();
		}

		byte[] magic = new byte[4];
		dis.readFully(magic, 0, magic.length);
		metadata.version = dis.readShort();
		byte encoding = dis.readByte();
		// System.out.println("Version: " + metadata.version);
		// System.out.println("Encoding: " + metadata.encoding);

		byte bStrategy = dis.readByte();
		switch (bStrategy) {
		default:
		case Constants.STRATEGY_DOUBLE:
			metadata.strategy = EncodingStrategy.STRATEGY_DOUBLE;
			break;
		case Constants.STRATEGY_INT_DELTA:
			metadata.strategy = EncodingStrategy.STRATEGY_INTEGER_DELTA;
			break;
		}

		double width = dis.readDouble();
		double height = dis.readDouble();
		image = new BvgImage(width, height);

		// System.out.println("Size: " + width + ", " + height);

		if (encoding == Constants.ENCODING_PLAIN) {
			metadata.encoding = EncodingMethod.PLAIN;
		} else if (encoding == Constants.ENCODING_DEFLATE) {
			metadata.encoding = EncodingMethod.DEFLATE;

			InflaterInputStream deflater = new InflaterInputStream(is);
			dis = new DataInputStream(deflater);
		} else if (encoding == Constants.ENCODING_LZ4) {
			metadata.encoding = EncodingMethod.LZ4;

			LZ4Factory factory = LZ4Factory.safeInstance();
			LZ4FastDecompressor decompressor = factory.fastDecompressor();

			LZ4BlockInputStream lz4 = new LZ4BlockInputStream(is, decompressor);
			dis = new DataInputStream(lz4);
		} else {
			throw new IOException("Illegal encoding");
		}

		BvgInputStream input;
		switch (metadata.strategy) {
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
