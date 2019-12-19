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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.bvg.BvgAwtPainter;
import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;

public class BvgToPng
{

	final static Logger logger = LoggerFactory.getLogger(BvgToPng.class);

	public static void main(String[] args) throws Exception
	{
		if (args.length != 2) {
			System.out.println("usage: " + BvgToPng.class.getSimpleName()
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

		BvgToPng test = new BvgToPng();
		test.createImage(bvg);
		test.finish(fileOutput);

		bos.close();
	}

	private BufferedImage image;

	public void createImage(BvgImage bvg)
	{
		double width = bvg.getWidth();
		double height = bvg.getHeight();
		int w = (int) Math.ceil(width);
		int h = (int) Math.ceil(height);
		image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		BvgAwtPainter.draw(g, bvg, 0, 0, 1, 1);
	}

	public void finish(File fileOutput) throws IOException
	{
		ImageIO.write(image, "png", fileOutput);
	}

}
