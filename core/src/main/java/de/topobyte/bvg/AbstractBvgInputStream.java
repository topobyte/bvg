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
import java.io.EOFException;
import java.io.IOException;

import de.topobyte.bvg.path.FillRule;

public abstract class AbstractBvgInputStream implements BvgInputStream
{

	protected BvgImage image;
	protected DataInputStream dis;

	public AbstractBvgInputStream(BvgImage image, DataInputStream dis)
	{
		this.image = image;
		this.dis = dis;
	}

	@Override
	public void read() throws IOException
	{
		while (true) {
			byte head;
			try {
				head = dis.readByte();
			} catch (EOFException e) {
				break;
			}
			if (head == Constants.ID_FILL) {
				readFill(FillRule.NON_ZERO);
			} else if (head == Constants.ID_FILL_EVEN_ODD) {
				readFill(FillRule.EVEN_ODD);
			} else if (head == Constants.ID_STROKE) {
				readStroke();
			}
		}

		dis.close();
	}

}
