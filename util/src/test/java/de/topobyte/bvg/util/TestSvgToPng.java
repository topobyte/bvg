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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import de.topobyte.bvg.Color;
import de.topobyte.bvg.LineStyle;
import de.topobyte.bvg.ToSwingUtil;
import de.topobyte.system.utils.SystemPaths;

public class TestSvgToPng implements ShapeSink
{

	private BufferedImage image;
	private Graphics2D g2d;
	private Path fileOutput;

	public static void main(String[] args) throws Exception
	{
		Path dirTest = SystemPaths.CWD.getParent().resolve("test");

		Path input = dirTest.resolve("shapes.svg");
		Path output = dirTest.resolve("shapes-direct.png");

		TestSvgToPng test = new TestSvgToPng(output);
		SvgParser svgParser = new SvgParser(test);
		svgParser.parseToSink(input);
	}

	public TestSvgToPng(Path output)
	{
		this.fileOutput = output;
	}

	@Override
	public void init(double width, double height)
	{
		int w = (int) Math.ceil(width);
		int h = (int) Math.ceil(height);
		image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);

		g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	public void finish() throws IOException
	{
		ImageIO.write(image, "png", fileOutput.toFile());
	}

	@Override
	public void fill(Shape shape, Color c)
	{
		java.awt.Color color = new java.awt.Color(c.getRed(), c.getGreen(),
				c.getBlue(), c.getAlpha());
		g2d.setColor(color);
		g2d.fill(shape);
	}

	@Override
	public void stroke(Shape shape, Color c, LineStyle lineStyle)
	{
		java.awt.Color color = new java.awt.Color(c.getRed(), c.getGreen(),
				c.getBlue(), c.getAlpha());
		g2d.setColor(color);

		BasicStroke stroke = null;

		if (lineStyle.getDashArray() == null) {
			stroke = new BasicStroke(lineStyle.getWidth(),
					ToSwingUtil.getCap(lineStyle.getCap()),
					ToSwingUtil.getJoin(lineStyle.getJoin()),
					lineStyle.getMiterLimit());
		} else {
			stroke = new BasicStroke(lineStyle.getWidth(),
					ToSwingUtil.getCap(lineStyle.getCap()),
					ToSwingUtil.getJoin(lineStyle.getJoin()),
					lineStyle.getMiterLimit(), lineStyle.getDashArray(),
					lineStyle.getDashOffset());
		}

		g2d.setStroke(stroke);
		g2d.draw(shape);
	}

}
