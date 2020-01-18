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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class RunBvgToBvg
{

	private final static String HELP_MESSAGE = RunBvgToBvg.class.getSimpleName()
			+ " [options] [input] [output]";

	private static void printHelpAndExit(Options options)
	{
		new HelpFormatter().printHelp(HELP_MESSAGE, options);
		System.exit(1);
	}

	public static void main(String[] args) throws Exception
	{

		Options options = new Options();
		BvgOutputToolUtil.addOptions(options);

		CommandLineParser parser = new DefaultParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			printHelpAndExit(options);
		}

		EncodingParameters parameters = null;
		try {
			parameters = BvgOutputToolUtil.parse(line);
		} catch (BvgOutputToolException e) {
			System.out.println(
					"Error while parsing command line: " + e.getMessage());
			printHelpAndExit(options);
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

		BvgToBvg bvgToBvg = new BvgToBvg(parameters.getMethod(),
				parameters.getStrategy());
		bvgToBvg.execute(fileInput, fileOutput);
	}

}