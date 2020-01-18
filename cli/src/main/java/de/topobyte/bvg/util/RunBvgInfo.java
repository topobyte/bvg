// Copyright 2020 Sebastian Kuerten
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

import java.io.File;

import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;
import de.topobyte.bvg.BvgMetadata;

public class RunBvgInfo
{

	public static void main(String[] args) throws Exception
	{
		if (args.length != 1) {
			System.out.println(
					"usage: " + RunBvgInfo.class.getSimpleName() + " [input]");
			System.exit(1);
		}

		String input = args[0];

		File fileInput = new File(input);

		BvgMetadata metadata = new BvgMetadata();
		BvgImage image = BvgIO.read(fileInput, metadata);

		BvgInfo test = new BvgInfo();
		test.printInfo(image, metadata);
	}

}
