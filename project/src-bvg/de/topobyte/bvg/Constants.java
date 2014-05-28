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

public class Constants
{

	// 4 magic bytes
	static final byte[] MAGIC = new byte[] { 98, 118, 103, 0 };

	static final short VERSION = 2;

	// width, height

	// encoding
	static final byte ENCODING_PLAIN = 0;
	static final byte ENCODING_DEFLATE = 1;

	// strategy
	static final byte STRATEGY_DOUBLE = 0;
	static final byte STRATEGY_INT_DELTA = 1;

	// element types
	static final byte ID_FILL = 0;
	static final byte ID_STROKE = 1;
	static final byte ID_FILL_STROKE = 2;

	// path construction
	static final byte PATH_MOVE_TO = 0;
	static final byte PATH_LINE_TO = 1;
	static final byte PATH_QUAD_TO = 2;
	static final byte PATH_CUBIC_TO = 3;
	static final byte PATH_CLOSE = 4;

	// line style
	static final byte CAP_BUTT = 0;
	static final byte CAP_ROUND = 1;
	static final byte CAP_SQUARE = 2;

	static final byte JOIN_BEVEL = 0;
	static final byte JOIN_MITER = 1;
	static final byte JOIN_ROUND = 2;

}
