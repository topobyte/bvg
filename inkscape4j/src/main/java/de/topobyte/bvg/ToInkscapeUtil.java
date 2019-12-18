// Copyright 2019 Sebastian Kuerten
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

import java.util.List;

import de.topobyte.bvg.path.CompactPath;
import de.topobyte.bvg.path.Type;
import de.topobyte.inkscape4j.ids.IdFactory;
import de.topobyte.inkscape4j.path.FillRule;
import de.topobyte.inkscape4j.path.Path;
import de.topobyte.inkscape4j.path.PathBuilder;
import de.topobyte.inkscape4j.style.LineCap;
import de.topobyte.inkscape4j.style.LineJoin;

public class ToInkscapeUtil
{

	public static LineCap getCap(Cap cap)
	{
		switch (cap) {
		default:
		case ROUND:
			return LineCap.ROUND;
		case BUTT:
			return LineCap.BUTT;
		case SQUARE:
			return LineCap.SQUARE;
		}
	}

	public static LineJoin getJoin(Join join)
	{
		switch (join) {
		default:
		case ROUND:
			return LineJoin.ROUND;
		case BEVEL:
			return LineJoin.BEVEL;
		case MITER:
			return LineJoin.MITER;
		}
	}

	private static FillRule getWindingRule(
			de.topobyte.bvg.path.FillRule fillRule)
	{
		switch (fillRule) {
		default:
		case EVEN_ODD:
			return FillRule.EVEN_ODD;
		case NON_ZERO:
			return FillRule.NON_ZERO;
		}
	}

	public static Path createPath(IdFactory idFactory, CompactPath path)
	{
		FillRule fillRule = getWindingRule(path.getFillRule());

		PathBuilder builder = new PathBuilder();

		List<Type> types = path.getTypes();
		double[] values = path.getValues();

		int i = 0;
		for (Type type : types) {
			switch (type) {
			case MOVE: {
				double x = values[i++];
				double y = values[i++];
				// System.out.println("MOVE " + x + " " + y);
				builder.move(false, x, y);
				break;
			}
			case CLOSE:
				builder.close();
				break;
			case LINE: {
				double x = values[i++];
				double y = values[i++];
				// System.out.println("LINE " + x + " " + y);
				builder.line(false, x, y);
				break;
			}
			case QUAD: {
				double x1 = values[i++];
				double y1 = values[i++];
				double x = values[i++];
				double y = values[i++];
				// System.out.println("QUAD " + x + " " + y);
				builder.quad(false, x1, y1, x, y);
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
				builder.cubic(false, x1, y1, x2, y2, x, y);
				break;
			}
			}
		}

		String id = idFactory.nextId();
		return builder.build(id, fillRule);
	}

}
