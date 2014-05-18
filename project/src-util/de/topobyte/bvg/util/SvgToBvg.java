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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import de.topobyte.bvg.BvgOutputStream;
import de.topobyte.bvg.Color;
import de.topobyte.bvg.Fill;
import de.topobyte.bvg.LineStyle;
import de.topobyte.bvg.Stroke;
import de.topobyte.bvg.path.Path;

public class SvgToBvg implements ShapeSink
{

	private final static String COMPRESS = "compress";

	private final static String HELP_MESSAGE = SvgToBvg.class.getSimpleName()
			+ "[options] [input] [output]";

	public static void main(String[] args) throws Exception
	{

		Options options = new Options();
		options.addOption(COMPRESS, false,
				"use deflate compression to compress the data stream");

		CommandLineParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);

		boolean compress = line.hasOption(COMPRESS);

		String[] extra = line.getArgs();
		if (extra.length != 2) {
			new HelpFormatter().printHelp(HELP_MESSAGE, options);
			System.out.println("usage: " + BvgToPng.class.getSimpleName()
					+ " [input] [output]");
			System.exit(1);
		}

		String input = extra[0];
		String output = extra[1];

		File fileInput = new File(input);

		File fileOutput = new File(output);
		File parentFile = fileOutput.getParentFile();
		if (parentFile != null) {
			parentFile.mkdirs();
		}

		FileOutputStream fos = new FileOutputStream(fileOutput);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		SvgToBvg svgToBvg = new SvgToBvg(bos, compress);
		SvgParser svgParser = new SvgParser(svgToBvg);
		svgParser.parseToSink(fileInput);

		bos.close();
	}

	private BvgOutputStream bvgOutputStream;
	private OutputStream os;

	private boolean compress;

	public SvgToBvg(OutputStream os, boolean compress) throws IOException
	{
		this.os = os;
		this.compress = compress;
	}

	@Override
	public void init(double width, double height) throws IOException
	{
		bvgOutputStream = new BvgOutputStream(os, compress, width, height);
	}

	@Override
	public void finish() throws IOException
	{
		bvgOutputStream.close();
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