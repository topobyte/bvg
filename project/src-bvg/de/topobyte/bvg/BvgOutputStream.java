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

import de.topobyte.bvg.path.Path;

public class BvgOutputStream
{

	private OutputStream os;
	private double width;
	private double height;

	private Fill fill;
	private Stroke stroke;

	private DataOutputStream dos;

	public BvgOutputStream(OutputStream os, double width, double height)
			throws IOException
	{
		this.os = os;
		this.width = width;
		this.height = height;

		dos = new DataOutputStream(os);

		// file header
		dos.write(Constants.MAGIC);
		dos.writeShort(Constants.VERSION);
		dos.writeByte(Constants.ENCODING_PLAIN);

		// image header
		dos.writeDouble(width);
		dos.writeDouble(height);
	}

	public void setFill(Fill fill)
	{
		this.fill = fill;
	}

	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}

	public void write(Path path)
	{

	}

}
