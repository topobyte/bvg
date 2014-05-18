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
import de.topobyte.bvg.Fill;
import de.topobyte.bvg.LineStyle;
import de.topobyte.bvg.Stroke;
import de.topobyte.bvg.path.Path;

public class SvgToBvg implements ShapeSink
{

	public static void main(String[] args) throws Exception
	{
		if (args.length != 2) {
			System.out.println("usage: " + SvgToBvg.class.getSimpleName()
					+ " [input] [output]");
			System.exit(1);
		}

		String input = args[0];
		String output = args[1];

		File fileInput = new File(input);

		File fileOutput = new File(output);
		File parentFile = fileOutput.getParentFile();
		parentFile.mkdirs();

		FileOutputStream fos = new FileOutputStream(fileOutput);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		SvgToBvg svgToBvg = new SvgToBvg(bos);
		SvgParser svgParser = new SvgParser(svgToBvg);
		svgParser.parseToSink(fileInput);

		bos.close();
	}

	private BvgOutputStream bvgOutputStream;
	private OutputStream os;

	public SvgToBvg(OutputStream os) throws IOException
	{
		this.os = os;
	}

	@Override
	public void init(double width, double height) throws IOException
	{
		bvgOutputStream = new BvgOutputStream(os, width, height);
	}

	@Override
	public void fill(Shape shape, Color color) throws IOException
	{
		Path path = SwingUtil.createPath(shape);

		Fill fill = new Fill(color);
		bvgOutputStream.fill(fill, path);
	}

	@Override
	public void stroke(Shape shape, Color color, LineStyle lineStyle)
			throws IOException
	{
		Path path = SwingUtil.createPath(shape);

		Stroke stroke = new Stroke(color, lineStyle);
		bvgOutputStream.stroke(stroke, path);
	}

}