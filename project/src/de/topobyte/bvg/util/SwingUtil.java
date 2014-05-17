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

import de.topobyte.bvg.Cap;
import de.topobyte.bvg.Join;

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
}
