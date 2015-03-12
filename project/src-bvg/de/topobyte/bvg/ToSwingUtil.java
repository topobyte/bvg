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

import java.awt.BasicStroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.List;

import de.topobyte.bvg.path.CompactPath;
import de.topobyte.bvg.path.Type;

public class ToSwingUtil
{
	public static int getCap(Cap cap)
	{
		switch (cap) {
		default:
		case ROUND:
			return BasicStroke.CAP_ROUND;
		case BUTT:
			return BasicStroke.CAP_BUTT;
		case SQUARE:
			return BasicStroke.CAP_SQUARE;
		}
	}

	public static int getJoin(Join join)
	{
		switch (join) {
		default:
		case ROUND:
			return BasicStroke.JOIN_ROUND;
		case BEVEL:
			return BasicStroke.JOIN_BEVEL;
		case MITER:
			return BasicStroke.JOIN_MITER;
		}
	}

	public static GeneralPath createPath(CompactPath path)
	{
		GeneralPath result = new GeneralPath(PathIterator.WIND_EVEN_ODD);

		List<Type> types = path.getTypes();
		double[] values = path.getValues();

		int i = 0;
		for (Type type : types) {
			switch (type) {
			case MOVE: {
				double x = values[i++];
				double y = values[i++];
				// System.out.println("MOVE " + x + " " + y);
				result.moveTo(x, y);
				break;
			}
			case CLOSE:
				result.closePath();
				break;
			case LINE: {
				double x = values[i++];
				double y = values[i++];
				// System.out.println("LINE " + x + " " + y);
				result.lineTo(x, y);
				break;
			}
			case QUAD: {
				double x1 = values[i++];
				double y1 = values[i++];
				double x = values[i++];
				double y = values[i++];
				// System.out.println("QUAD " + x + " " + y);
				result.quadTo(x1, y1, x, y);
				break;
			}
			case CUBIC: {
				double x1 = values[i++];
				double y1 = values[i++];
				double x2 = values[i++];
				double y2 = values[i++];
				double x = values[i++];
				double y = values[i++];
				// System.out.println("CUBIC " + x + " " + y);
				result.curveTo(x1, y1, x2, y2, x, y);
				break;
			}
			}
		}

		return result;
	}
}
