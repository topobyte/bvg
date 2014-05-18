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
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import de.topobyte.bvg.BvgImage;
import de.topobyte.bvg.Fill;
import de.topobyte.bvg.IColor;
import de.topobyte.bvg.LineStyle;
import de.topobyte.bvg.PaintElement;
import de.topobyte.bvg.Stroke;
import de.topobyte.bvg.path.CompactPath;

public class BvgToPng
{

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

			System.out.println("PATH");
			GeneralPath p = SwingUtil.createPath(path);

			if (element instanceof Fill) {
				Fill fill = (Fill) element;
				IColor color = fill.getColor();
				System.out
						.println(String.format("0x%08X", color.getColorCode()));
				Color c = new Color(color.getRed(), color.getGreen(),
						color.getBlue(), color.getAlpha());

				g.setColor(c);

				g.fill(p);
			} else if (element instanceof Stroke) {
				Stroke stroke = (Stroke) element;

				IColor color = stroke.getColor();
				Color c = new Color(color.getRed(), color.getGreen(),
						color.getBlue(), color.getAlpha());
				g.setColor(c);

				LineStyle lineStyle = stroke.getLineStyle();
				int cap = SwingUtil.getCap(lineStyle.getCap());
				int join = SwingUtil.getJoin(lineStyle.getJoin());
				BasicStroke bs = new BasicStroke(lineStyle.getWidth(), cap,
						join, lineStyle.getMiterLimit());
				g.setStroke(bs);

				g.draw(p);
			}
		}
	}

	public void finish(File fileOutput) throws IOException
	{
		ImageIO.write(image, "png", fileOutput);
	}
}
