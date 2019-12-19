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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BvgIO
{

	public static BvgImage read(File file) throws IOException
	{
		return read(file, null);
	}

	public static BvgImage read(InputStream is) throws IOException
	{
		return read(is, null);
	}

	public static BvgImage read(File file, BvgMetadata metadata)
			throws IOException
	{
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream is = new BufferedInputStream(fis);
		BvgImage image = read(is, metadata);
		is.close();
		return image;
	}

	public static BvgImage read(InputStream is, BvgMetadata metadata)
			throws IOException
	{
		BvgReader reader = new BvgReader(is);
		reader.read(metadata);
		return reader.image;
	}

	public static void write(BvgImage image, File file, EncodingMethod method,
			EncodingStrategy strategy) throws IOException
	{
		BvgWriter writer = new BvgWriter(image, method, strategy);
		writer.write(file);
	}

	public static void write(BvgImage image, OutputStream output,
			EncodingMethod method, EncodingStrategy strategy) throws IOException
	{
		BvgWriter writer = new BvgWriter(image, method, strategy);
		writer.write(output);
	}

}
