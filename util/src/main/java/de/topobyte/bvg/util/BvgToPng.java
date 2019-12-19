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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.bvg.BvgIO;
import de.topobyte.bvg.BvgImage;
import de.topobyte.bvg.Fill;
import de.topobyte.bvg.IColor;
import de.topobyte.bvg.LineStyle;
import de.topobyte.bvg.PaintElement;
import de.topobyte.bvg.Stroke;
import de.topobyte.bvg.ToSwingUtil;
import de.topobyte.bvg.path.CompactPath;

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

		List<PaintElement> elements = bvg.getPaintElements();
		List<CompactPath> paths = bvg.getPaths();

		for (int i = 0; i < elements.size(); i++) {
			PaintElement element = elements.get(i);
			CompactPath path = paths.get(i);

			logger.debug("PATH");
			GeneralPath p = ToSwingUtil.createPath(path);

			if (element instanceof Fill) {
				logger.debug("FILL");
				Fill fill = (Fill) element;
				IColor color = fill.getColor();
				logger.debug(String.format("0x%08X", color.getColorCode()));
				Color c = new Color(color.getRed(), color.getGreen(),
						color.getBlue(), color.getAlpha());

				g.setColor(c);

				g.fill(p);
			} else if (element instanceof Stroke) {
				logger.debug("STROKE");
				Stroke stroke = (Stroke) element;

				IColor color = stroke.getColor();
				logger.debug(String.format("0x%08X", color.getColorCode()));
				Color c = new Color(color.getRed(), color.getGreen(),
						color.getBlue(), color.getAlpha());
				g.setColor(c);

				LineStyle lineStyle = stroke.getLineStyle();
				int cap = ToSwingUtil.getCap(lineStyle.getCap());
				int join = ToSwingUtil.getJoin(lineStyle.getJoin());
				float[] dashArray = lineStyle.getDashArray();
				float dashOffset = lineStyle.getDashOffset();

				BasicStroke bs;
				if (dashArray == null) {
					bs = new BasicStroke(lineStyle.getWidth(), cap, join,
							lineStyle.getMiterLimit());
				} else {
					bs = new BasicStroke(lineStyle.getWidth(), cap, join,
							lineStyle.getMiterLimit(), dashArray, dashOffset);
				}
				g.setStroke(bs);

				logger.debug("Linewidth: " + lineStyle.getWidth());
				logger.debug("Join: " + lineStyle.getJoin());
				logger.debug("Cap: " + lineStyle.getCap());
				logger.debug("Miter limit: " + lineStyle.getMiterLimit());

				g.draw(p);
			}
		}
	}

	public void finish(File fileOutput) throws IOException
	{
		ImageIO.write(image, "png", fileOutput);
	}

}
