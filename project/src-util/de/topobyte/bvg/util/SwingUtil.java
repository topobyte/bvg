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
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import de.topobyte.bvg.Cap;
import de.topobyte.bvg.Join;
import de.topobyte.bvg.path.Close;
import de.topobyte.bvg.path.CompactPath;
import de.topobyte.bvg.path.CubicTo;
import de.topobyte.bvg.path.LineTo;
import de.topobyte.bvg.path.MoveTo;
import de.topobyte.bvg.path.Path;
import de.topobyte.bvg.path.PathElement;
import de.topobyte.bvg.path.QuadTo;
import de.topobyte.bvg.path.Type;

public class SwingUtil
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

	public static Path createPath(Shape shape)
	{
		PathIterator it = shape.getPathIterator(null);

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

		return new Path(types, elements);
	}

	public static GeneralPath createPath(CompactPath path)
	{
		GeneralPath result = new GeneralPath();

		List<Type> types = path.getTypes();
		double[] values = path.getValues();

		int i = 0;
		for (Type type : types) {
			switch (type) {
			case MOVE: {
				double x = values[i++];
				double y = values[i++];
				System.out.println("MOVE " + x + " " + y);
				result.moveTo(x, y);
				break;
			}
			case CLOSE:
				result.closePath();
				break;
			case LINE: {
				double x = values[i++];
				double y = values[i++];
				System.out.println("LINE " + x + " " + y);
				result.lineTo(x, y);
				break;
			}
			case QUAD: {
				double x1 = values[i++];
				double y1 = values[i++];
				double x = values[i++];
				double y = values[i++];
				System.out.println("QUAD " + x + " " + y);
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
				System.out.println("CUBIC " + x + " " + y);
				result.curveTo(x1, y1, x2, y2, x, y);
				break;
			}
			}
		}

		return result;
	}
}
