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
import de.topobyte.bvg.BvgOutputStreamDouble;
import de.topobyte.bvg.BvgOutputStreamIntegerDelta;
import de.topobyte.bvg.Color;
import de.topobyte.bvg.EncodingStrategy;
import de.topobyte.bvg.Fill;
import de.topobyte.bvg.LineStyle;
import de.topobyte.bvg.Stroke;
import de.topobyte.bvg.path.Path;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

public class SvgToBvg implements ShapeSink
{

	private final static String OPTION_COMPRESS = "compress";
	private final static String OPTION_STRATEGY = "strategy";

	private final static String HELP_MESSAGE = SvgToBvg.class.getSimpleName()
			+ "[options] [input] [output]";

	private static void printHelpAndExit(Options options)
	{
		new HelpFormatter().printHelp(HELP_MESSAGE, options);
		System.out.println("usage: " + BvgToPng.class.getSimpleName()
				+ " [input] [output]");
		System.exit(1);
	}

	public static void main(String[] args) throws Exception
	{

		Options options = new Options();
		OptionHelper.add(options, OPTION_COMPRESS, false, false,
				"compress using deflate");
		OptionHelper.add(options, OPTION_STRATEGY, true, false,
				"double, int_delta");

		CommandLineParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);

		boolean compress = line.hasOption(OPTION_COMPRESS);

		EncodingStrategy strategy = EncodingStrategy.STRATEGY_DOUBLE;
		if (line.hasOption(OPTION_STRATEGY)) {
			String value = line.getOptionValue(OPTION_STRATEGY);
			if (value.equals("double")) {
				strategy = EncodingStrategy.STRATEGY_DOUBLE;
			} else if (value.equals("int_delta")) {
				strategy = EncodingStrategy.STRATEGY_INTEGER_DELTA;
			} else {
				System.out.println("unknown encoding strategy");
				printHelpAndExit(options);
			}
		}

		String[] extra = line.getArgs();
		if (extra.length != 2) {
			printHelpAndExit(options);
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

		SvgToBvg svgToBvg = new SvgToBvg(bos, compress, strategy);
		SvgParser svgParser = new SvgParser(svgToBvg);
		svgParser.parseToSink(fileInput);

		bos.close();
	}

	private BvgOutputStream bvgOutputStream;
	private OutputStream os;

	private boolean compress;
	private EncodingStrategy strategy;

	public SvgToBvg(OutputStream os, boolean compress, EncodingStrategy strategy)
			throws IOException
	{
		this.os = os;
		this.compress = compress;
		this.strategy = strategy;
	}

	@Override
	public void init(double width, double height) throws IOException
	{
		switch (strategy) {
		default:
		case STRATEGY_DOUBLE:
			bvgOutputStream = new BvgOutputStreamDouble(os, compress, width,
					height);
			break;
		case STRATEGY_INTEGER_DELTA:
			bvgOutputStream = new BvgOutputStreamIntegerDelta(os, compress,
					width, height);
			break;
		}
	}

	@Override
	public void finish() throws IOException
	{
		bvgOutputStream.close();
	}

	@Override
	public void fill(Shape shape, Color color) throws IOException
	{
		Path path = FromSwingUtil.createPath(shape);

		Fill fill = new Fill(color);
		bvgOutputStream.fill(fill, path);
	}

	@Override
	public void stroke(Shape shape, Color color, LineStyle lineStyle)
			throws IOException
	{
		Path path = FromSwingUtil.createPath(shape);

		Stroke stroke = new Stroke(color, lineStyle);
		bvgOutputStream.stroke(stroke, path);
	}

}