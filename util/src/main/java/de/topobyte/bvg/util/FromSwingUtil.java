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

package de.topobyte.bvg.util;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import de.topobyte.bvg.Cap;
import de.topobyte.bvg.Join;
import de.topobyte.bvg.path.Close;
import de.topobyte.bvg.path.CubicTo;
import de.topobyte.bvg.path.FillRule;
import de.topobyte.bvg.path.LineTo;
import de.topobyte.bvg.path.MoveTo;
import de.topobyte.bvg.path.Path;
import de.topobyte.bvg.path.PathElement;
import de.topobyte.bvg.path.QuadTo;
import de.topobyte.bvg.path.Type;

public class FromSwingUtil
{

	public static Cap getCap(int cap)
	{
		switch (cap) {
		default:
		case BasicStroke.CAP_ROUND:
			return Cap.ROUND;
		case BasicStroke.CAP_BUTT:
			return Cap.BUTT;
		case BasicStroke.CAP_SQUARE:
			return Cap.SQUARE;
		}
	}

	public static Join getJoin(int join)
	{
		switch (join) {
		default:
		case BasicStroke.JOIN_ROUND:
			return Join.ROUND;
		case BasicStroke.JOIN_BEVEL:
			return Join.BEVEL;
		case BasicStroke.JOIN_MITER:
			return Join.MITER;
		}
	}

	public static Path createPath(Shape shape)
	{
		PathIterator it = shape.getPathIterator(null);

		FillRule fillRule = FillRule.NON_ZERO;
		int windingRule = it.getWindingRule();
		if (windingRule == PathIterator.WIND_NON_ZERO) {
			fillRule = FillRule.NON_ZERO;
		} else if (windingRule == PathIterator.WIND_EVEN_ODD) {
			fillRule = FillRule.EVEN_ODD;
		}

		double points[] = new double[6];

		List<Type> types = new ArrayList<Type>();
		List<PathElement> elements = new ArrayList<PathElement>();

		while (!it.isDone()) {
			int type = it.currentSegment(points);
			switch (type) {
			case PathIterator.SEG_MOVETO:
				types.add(Type.MOVE);
				elements.add(new MoveTo(points[0], points[1]));
				break;
			case PathIterator.SEG_CLOSE:
				types.add(Type.CLOSE);
				elements.add(new Close());
				break;
			case PathIterator.SEG_LINETO:
				types.add(Type.LINE);
				elements.add(new LineTo(points[0], points[1]));
				break;
			case PathIterator.SEG_QUADTO:
				types.add(Type.QUAD);
				elements.add(new QuadTo(points[0], points[1], points[2],
						points[3]));
				break;
			case PathIterator.SEG_CUBICTO:
				types.add(Type.CUBIC);
				elements.add(new CubicTo(points[0], points[1], points[2],
						points[3], points[4], points[5]));
				break;
			}
			it.next();
		}

		return new Path(fillRule, types, elements);
	}

}
