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
	static final byte[] magic = new byte[] { 98, 118, 103, 0 };

	// width, height

	// encoding
	static final byte ENCODING_PLAIN = 0;
	static final byte ENCODING_DEFLATE = 1;

	// color type
	static final byte COLOR_TYPE_GRAYSCALE = 0;
	static final byte COLOR_TYPE_RGB = 1;
	static final byte COLOR_TYPE_PALETTE = 2;

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

}
