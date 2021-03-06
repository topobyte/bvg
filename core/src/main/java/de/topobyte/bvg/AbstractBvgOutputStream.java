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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

import de.topobyte.bvg.path.Path;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

public abstract class AbstractBvgOutputStream implements BvgOutputStream
{

	protected OutputStream os;
	protected DataOutputStream dos;

	protected double width;
	protected double height;

	public AbstractBvgOutputStream(OutputStream os, EncodingMethod method,
			EncodingStrategy strategy, double width, double height)
			throws IOException
	{
		this.os = os;
		this.width = width;
		this.height = height;

		dos = new DataOutputStream(os);

		// file header
		dos.write(Constants.MAGIC);
		dos.writeShort(Constants.VERSION);
		dos.writeByte(method.getByte());

		switch (strategy) {
		default:
		case STRATEGY_DOUBLE:
			dos.writeByte(Constants.STRATEGY_DOUBLE);
			break;
		case STRATEGY_INTEGER_DELTA:
			dos.writeByte(Constants.STRATEGY_INT_DELTA);
			break;
		}

		// image header
		dos.writeDouble(width);
		dos.writeDouble(height);

		if (method == EncodingMethod.DEFLATE) {
			dos.flush();

			DeflaterOutputStream deflater = new DeflaterOutputStream(os);
			dos = new DataOutputStream(deflater);
		} else if (method == EncodingMethod.LZ4) {
			LZ4Factory factory = LZ4Factory.safeInstance();
			LZ4Compressor compressor = factory.fastCompressor();
			compressor = factory.highCompressor();

			LZ4BlockOutputStream lz4 = new LZ4BlockOutputStream(os, 1 << 16,
					compressor);
			dos = new DataOutputStream(lz4);
		}
	}

	protected void writeFillCode(Path path) throws IOException
	{
		switch (path.getFillRule()) {
		default:
		case NON_ZERO:
			dos.writeByte(Constants.ID_FILL);
			break;
		case EVEN_ODD:
			dos.writeByte(Constants.ID_FILL_EVEN_ODD);
			break;
		}
	}

	@Override
	public void close() throws IOException
	{
		dos.close();
	}

}
