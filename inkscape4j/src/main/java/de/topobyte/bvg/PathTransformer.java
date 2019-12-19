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

import de.topobyte.inkscape4j.Style;
import de.topobyte.inkscape4j.path.CubicTo;
import de.topobyte.inkscape4j.path.LineTo;
import de.topobyte.inkscape4j.path.MoveTo;
import de.topobyte.inkscape4j.path.Path;
import de.topobyte.inkscape4j.path.PathElement;
import de.topobyte.inkscape4j.path.QuadTo;
import de.topobyte.inkscape4j.path.Type;

public class PathTransformer
{

	private float tx;
	private float ty;
	private float sx;
	private float sy;

	public PathTransformer(float tx, float ty, float sx, float sy)
	{
		this.tx = tx;
		this.ty = ty;
		this.sx = sx;
		this.sy = sy;
	}

	public void transform(Path p)
	{
		double scale = (sx + sy) / 2;

		Style style = p.getStyle();
		if (style != null) {
			style.setStrokeWidth(style.getStrokeWidth() * scale);

			if (style.getDashArray().isPresent()) {
				float[] dashArray = style.getDashArray().get();
				DashArrays.scale(dashArray, scale);
			}
			if (style.getDashOffset().isPresent()) {
				style.setDashOffset(
						(float) (style.getDashOffset().get() * scale));
			}
		}

		List<PathElement> elements = p.getElements();
		for (int i = 0; i < elements.size(); i++) {
			PathElement element = elements.get(i);
			Type type = element.getType();
			switch (type) {
			case MOVE:
				MoveTo move = (MoveTo) element;
				elements.set(i, new MoveTo(move.isRelative(),
						transformX(move.getX()), transformY(move.getY())));
				break;
			case LINE:
				LineTo line = (LineTo) element;
				elements.set(i, new LineTo(line.isRelative(),
						transformX(line.getX()), transformY(line.getY())));
				break;
			case QUAD:
				QuadTo quad = (QuadTo) element;
				elements.set(i, new QuadTo(quad.isRelative(),
						transformX(quad.getCX()), transformY(quad.getCY()),
						transformX(quad.getX()), transformY(quad.getY())));
				break;
			case CUBIC:
				CubicTo cubic = (CubicTo) element;
				elements.set(i, new CubicTo(cubic.isRelative(),
						transformX(cubic.getCX1()), transformY(cubic.getCY1()),
						transformX(cubic.getCX2()), transformY(cubic.getCY2()),
						transformX(cubic.getX()), transformY(cubic.getY())));
				break;
			default:
				break;

			}
		}
	}

	private double transformX(double x)
	{
		return tx + x * sx;
	}

	private double transformY(double y)
	{
		return ty + y * sy;
	}

}
