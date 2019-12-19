// Copyright 2015 Sebastian Kuerten
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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import de.topobyte.bvg.EncodingMethod;
import de.topobyte.bvg.EncodingStrategy;
import de.topobyte.utilities.apache.commons.cli.OptionHelper;

public class BvgOutputToolUtil
{

	private final static String OPTION_COMPRESS = "compress";
	private final static String OPTION_STRATEGY = "strategy";

	public static void addOptions(Options options)
	{
		OptionHelper.addL(options, OPTION_COMPRESS, true, false,
				"[plain], deflate, lz4");
		OptionHelper.addL(options, OPTION_STRATEGY, true, false,
				"[double], int_delta");
	}

	public static EncodingParameters parse(CommandLine line)
			throws BvgOutputToolException
	{
		EncodingMethod method = EncodingMethod.PLAIN;
		if (line.hasOption(OPTION_COMPRESS)) {
			String value = line.getOptionValue(OPTION_COMPRESS);
			if (value.equals("plain")) {
				method = EncodingMethod.PLAIN;
			} else if (value.equals("deflate")) {
				method = EncodingMethod.DEFLATE;
			} else if (value.equals("lz4")) {
				method = EncodingMethod.LZ4;
			} else {
				throw new BvgOutputToolException(
						"unknown encoding method (plain|deflate|lz4)");
			}
		}

		EncodingStrategy strategy = EncodingStrategy.STRATEGY_DOUBLE;
		if (line.hasOption(OPTION_STRATEGY)) {
			String value = line.getOptionValue(OPTION_STRATEGY);
			if (value.equals("double")) {
				strategy = EncodingStrategy.STRATEGY_DOUBLE;
			} else if (value.equals("int_delta")) {
				strategy = EncodingStrategy.STRATEGY_INTEGER_DELTA;
			} else {
				throw new BvgOutputToolException(
						"unknown encoding strategy (double|int_delta)");
			}
		}

		return new EncodingParameters(method, strategy);
	}

}
