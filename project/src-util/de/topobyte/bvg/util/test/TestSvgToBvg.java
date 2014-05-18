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

package de.topobyte.bvg.util.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import de.topobyte.bvg.util.SvgParser;
import de.topobyte.bvg.util.SvgToBvg;

public class TestSvgToBvg
{

	public static void main(String[] args) throws Exception
	{
		String input = "/home/z/git/map-icons/test/hotel.svg";
		String output = "/home/z/git/map-icons/test/hotel.bvg";

		File fileInput = new File(input);

		File fileOutput = new File(output);
		File parentFile = fileOutput.getParentFile();
		parentFile.mkdirs();

		FileOutputStream fos = new FileOutputStream(fileOutput);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		SvgToBvg svgToBvg = new SvgToBvg(bos, false);
		SvgParser svgParser = new SvgParser(svgToBvg);
		svgParser.parseToSink(fileInput);

		bos.close();
	}

}
