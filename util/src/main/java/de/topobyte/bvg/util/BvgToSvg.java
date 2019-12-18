// Copyright 2019 Sebastian Kuerten
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;
import de.topobyte.bvg.BvgInkscapePainter;
import de.topobyte.inkscape4j.Layer;
import de.topobyte.inkscape4j.SvgFile;
import de.topobyte.inkscape4j.SvgFileWriting;
import de.topobyte.inkscape4j.ids.IdFactory;
import de.topobyte.inkscape4j.ids.SimpleIdFactory;

public class BvgToSvg
{

	public static void main(String[] args) throws Exception
	{
		if (args.length != 2) {
			System.out.println("usage: " + BvgToSvg.class.getSimpleName()
					+ " [input] [output]");
			System.exit(1);
		}

		String input = args[0];
		String output = args[1];

		File fileInput = new File(input);

		File fileOutput = new File(output);
		File parentFile = fileOutput.getParentFile();
		if (parentFile != null) {
			parentFile.mkdirs();
		}

		FileOutputStream fos = new FileOutputStream(fileOutput);
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		BvgImage bvg = BvgIO.read(fileInput);

		BvgToSvg test = new BvgToSvg();
		test.createImage(bvg);
		test.finish(fileOutput);

		bos.close();
	}

	private SvgFile svg;

	public void createImage(BvgImage bvg)
	{
		svg = new SvgFile();

		double width = bvg.getWidth();
		double height = bvg.getHeight();

		svg.setWidth(String.format("%fpx", width));
		svg.setHeight(String.format("%fpx", height));

		Layer layer = new Layer("laer");
		svg.getLayers().add(layer);
		layer.setLabel("Layer");

		IdFactory idFactory = new SimpleIdFactory();

		BvgInkscapePainter.draw(idFactory, layer, bvg, 0, 0, (float) width,
				(float) height);
	}

	public void finish(File fileOutput) throws IOException
	{
		OutputStream fos = new FileOutputStream(fileOutput);
		SvgFileWriting.write(svg, fos);
		fos.close();
	}

}
