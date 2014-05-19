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

import java.util.List;

import android.graphics.Paint;
import android.graphics.Path;
import de.topobyte.bvg.path.CompactPath;
import de.topobyte.bvg.path.Type;

public class ToAndroidUtil
{

	public static Paint.Cap getCap(Cap cap)
	{
		switch (cap) {
		default:
		case ROUND:
			return Paint.Cap.ROUND;
		case BUTT:
			return Paint.Cap.BUTT;
		case SQUARE:
			return Paint.Cap.SQUARE;
		}
	}

	public static Paint.Join getJoin(Join join)
	{
		switch (join) {
		default:
		case ROUND:
			return Paint.Join.ROUND;
		case BEVEL:
			return Paint.Join.BEVEL;
		case MITER:
			return Paint.Join.MITER;
		}
	}

	public static Path createPath(CompactPath path)
	{
		Path p = new Path();

		List<Type> types = path.getTypes();
		double[] values = path.getValues();

		int i = 0;
		for (Type type : types) {
			switch (type) {
			case MOVE: {
				double x = values[i++];
				double y = values[i++];
				p.moveTo((float) x, (float) y);
				break;
			}
			case CLOSE:
				p.close();
				break;
			case LINE: {
				double x = values[i++];
				double y = values[i++];
				p.lineTo((float) x, (float) y);
				break;
			}
			case QUAD: {
				double x1 = values[i++];
				double y1 = values[i++];
				double x = values[i++];
				double y = values[i++];
				p.quadTo((float) x1, (float) y1, (float) x, (float) y);
				break;
			}
			case CUBIC: {
				double x1 = values[i++];
				double y1 = values[i++];
				double x2 = values[i++];
				double y2 = values[i++];
				double x = values[i++];
				double y = values[i++];
				p.cubicTo((float) x1, (float) y1, (float) x2, (float) y2,
						(float) x, (float) y);
				break;
			}
			}
		}

		return p;
	}

}
