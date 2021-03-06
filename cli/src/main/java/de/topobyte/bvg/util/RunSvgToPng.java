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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.RootGraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.svg.SVGDocument;

import de.topobyte.utilities.apache.commons.cli.OptionHelper;

public class RunSvgToPng
{

	private final static String OPTION_GVT = "gvt";

	private final static String HELP_MESSAGE = RunSvgToPng.class.getSimpleName()
			+ "[options] [input] [output]";

	private static void printHelpAndExit(Options options)
	{
		new HelpFormatter().printHelp(HELP_MESSAGE, options);
		System.out.println("usage: " + RunBvgToPng.class.getSimpleName()
				+ " [input] [output]");
		System.exit(1);
	}

	public static void main(String[] args) throws Exception
	{
		Options options = new Options();
		OptionHelper.addL(options, OPTION_GVT, false, false,
				"use GVT painting instead of parser");

		CommandLineParser parser = new DefaultParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			printHelpAndExit(options);
		}

		boolean useGVT = line.hasOption(OPTION_GVT);

		String[] extra = line.getArgs();
		if (extra.length != 2) {
			printHelpAndExit(options);
		}

		String input = extra[0];
		String output = extra[1];

		java.nio.file.Path fileInput = Paths.get(input);

		File fileOutput = new File(output);
		File parentFile = fileOutput.getParentFile();
		if (parentFile != null) {
			parentFile.mkdirs();
		}

		if (!useGVT) {
			SvgToPng svgToBvg = new SvgToPng(fileOutput);
			SvgParser svgParser = new SvgParser(svgToBvg);
			svgParser.parseToSink(fileInput);
		} else {
			String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
			SVGDocument doc = df
					.createSVGDocument(fileInput.toFile().toURI().toString());
			UserAgent userAgent = new UserAgentAdapter();
			DocumentLoader loader = new DocumentLoader(userAgent);
			BridgeContext ctx = new BridgeContext(userAgent, loader);
			ctx.setDynamicState(BridgeContext.DYNAMIC);
			GVTBuilder builder = new GVTBuilder();
			RootGraphicsNode rootGN = (RootGraphicsNode) builder.build(ctx,
					doc);

			Dimension2D docSize = ctx.getDocumentSize();
			double width = docSize.getWidth();
			double height = docSize.getHeight();

			int w = (int) Math.ceil(width);
			int h = (int) Math.ceil(height);

			BufferedImage image = new BufferedImage(w, h,
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			rootGN.paint(g);

			ImageIO.write(image, "png", fileOutput);
		}
	}

}