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

import de.topobyte.bvg.BvgImage;
import de.topobyte.bvg.BvgMetadata;

public class BvgInfo
{

	public void printInfo(BvgImage image, BvgMetadata metadata)
	{
		System.out.println("Width: " + image.getWidth());
		System.out.println("Height: " + image.getHeight());
		System.out.println("Encoding method: " + metadata.getEncodingMethod());
		System.out.println(
				"Encoding strategy: " + metadata.getEncodingStrategy());
		System.out.println("Version: " + metadata.getVersion());
	}

}
