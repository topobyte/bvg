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

public enum EncodingMethod {

	PLAIN(Constants.ENCODING_PLAIN), //
	DEFLATE(Constants.ENCODING_DEFLATE), //
	LZ4(Constants.ENCODING_LZ4);

	private final int value;

	private EncodingMethod(int value)
	{
		this.value = value;
	}

	public int getByte()
	{
		return value;
	}
}
