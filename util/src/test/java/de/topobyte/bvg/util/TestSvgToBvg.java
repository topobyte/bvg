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

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import de.topobyte.bvg.EncodingMethod;
import de.topobyte.bvg.EncodingStrategy;
import de.topobyte.system.utils.SystemPaths;

public class TestSvgToBvg
{

	public static void main(String[] args) throws Exception
	{
		Path dirTest = SystemPaths.CWD.getParent().resolve("test");

		Path input = dirTest.resolve("shapes.svg");
		Path output = dirTest.resolve("shapes.bvg");

		OutputStream fos = Files.newOutputStream(output);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		SvgToBvg svgToBvg = new SvgToBvg(bos, EncodingMethod.PLAIN,
				EncodingStrategy.STRATEGY_DOUBLE);
		SvgParser svgParser = new SvgParser(svgToBvg);
		svgParser.parseToSink(input);

		bos.close();
	}

}
