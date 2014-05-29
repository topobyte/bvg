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

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import de.topobyte.bvg.BvgImage;
import de.topobyte.bvg.BvgReader;
import de.topobyte.bvg.BvgWriter;
import de.topobyte.bvg.EncodingStrategy;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

public class BvgToBvg
{

	private final static String OPTION_COMPRESS = "compress";
	private final static String OPTION_STRATEGY = "strategy";

	private final static String HELP_MESSAGE = BvgToBvg.class.getSimpleName()
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

		BvgToBvg bvgToBvg = new BvgToBvg(compress, strategy);
		bvgToBvg.execute(fileInput, fileOutput);
	}

	private boolean compress;
	private EncodingStrategy strategy;

	public BvgToBvg(boolean compress, EncodingStrategy strategy)
			throws IOException
	{
		this.compress = compress;
		this.strategy = strategy;
	}

	private void execute(File fileInput, File fileOutput) throws IOException
	{
		BvgImage image = BvgReader.read(fileInput);
		BvgWriter writer = new BvgWriter(image, compress, strategy);
		writer.write(fileOutput);
	}

}