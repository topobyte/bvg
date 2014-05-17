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

import java.awt.Shape;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.topobyte.bvg.BvgOutputStream;
import de.topobyte.bvg.Color;
import de.topobyte.bvg.LineStyle;

public class SvgToBvg implements ShapeSink
{

	public static void main(String[] args) throws Exception
	{
		String output = "/home/z/git/map-icons/bvg/bakery.png";
		File fileOutput = new File(output);
		File parentFile = fileOutput.getParentFile();
		parentFile.mkdirs();

		FileOutputStream fos = new FileOutputStream(fileOutput);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		String input = "/home/z/git/map-icons/simple/bakery.svg";
		File fileInput = new File(input);

		SvgToBvg svgToBvg = new SvgToBvg(bos);
		SvgParser svgParser = new SvgParser(svgToBvg);
		svgParser.parseToSink(fileInput);

		bos.close();
	}

	private BvgOutputStream bvgOutputStream;

	public SvgToBvg(OutputStream os) throws IOException
	{
		int width = 800, height = 800;
		bvgOutputStream = new BvgOutputStream(os, width, height);
	}

	@Override
	public void init(double width, double height)
	{
		// TODO: implement
	}

	@Override
	public void fill(Shape shape, Color color)
	{
		bvgOutputStream.setFill(null);
		bvgOutputStream.write(shape);
	}

	@Override
	public void stroke(Shape shape, Color color, LineStyle lineStyle)
	{
		bvgOutputStream.setStroke(null);
		bvgOutputStream.write(shape);
	}

}