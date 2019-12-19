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

import java.io.IOException;
import java.nio.file.Path;

import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;
import de.topobyte.system.utils.SystemPaths;

public class TestBvgToPng
{

	public static void main(String[] args) throws IOException
	{
		Path dirTest = SystemPaths.CWD.getParent().resolve("test");

		Path input = dirTest.resolve("shapes.bvg");
		Path output = dirTest.resolve("shapes.png");

		BvgImage bvg = BvgIO.read(input.toFile());

		BvgToPng test = new BvgToPng();
		test.createImage(bvg);
		test.finish(output.toFile());
	}

}
