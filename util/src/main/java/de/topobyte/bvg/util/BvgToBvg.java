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

import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;
import de.topobyte.bvg.EncodingMethod;
import de.topobyte.bvg.EncodingStrategy;

public class BvgToBvg
{

	private final EncodingMethod method;
	private final EncodingStrategy strategy;

	public BvgToBvg(EncodingMethod method, EncodingStrategy strategy)
			throws IOException
	{
		this.method = method;
		this.strategy = strategy;
	}

	void execute(File fileInput, File fileOutput) throws IOException
	{
		BvgImage image = BvgIO.read(fileInput);
		BvgIO.write(image, fileOutput, method, strategy);
	}

}