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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.CompositeShapePainter;
import org.apache.batik.gvt.FillShapePainter;
import org.apache.batik.gvt.RootGraphicsNode;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.apache.batik.gvt.StrokeShapePainter;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

public class Test
{
	public static void main(String[] args) throws Exception
	{
		String input = "/home/z/git/map-icons/simple/bakery.svg";
		String output = "/home/z/git/map-icons/bvg/bakery.png";

		File fileInput = new File(input);
		File fileOutput = new File(output);

		String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
		SVGDocument doc = df.createSVGDocument(fileInput.toURL().toString());
		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(userAgent);
		BridgeContext ctx = new BridgeContext(userAgent, loader);
		ctx.setDynamicState(BridgeContext.DYNAMIC);
		GVTBuilder builder = new GVTBuilder();
		RootGraphicsNode rootGN = (RootGraphicsNode) builder.build(ctx, doc);

		System.out.println(rootGN);
		System.out.println(rootGN.getGeometryBounds());
		System.out.println(rootGN.getOutline());

		BufferedImage image = new BufferedImage(800, 800,
				BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		System.out.println("TRAVERSAL");
		go(rootGN, 0, g2d);

		ImageIO.write(image, "png", fileOutput);
	}

	private static void go(CompositeGraphicsNode cgn, int level, Graphics2D g2d)
	{
		print(cgn.toString(), level);
		List<?> children = cgn.getChildren();
		for (Object child : children) {
			if (child instanceof CompositeGraphicsNode) {
				go((CompositeGraphicsNode) child, level + 1, g2d);
			} else {
				print("LEAF: " + child.toString(), level);
				if (child instanceof ShapeNode) {
					ShapeNode sn = (ShapeNode) child;
					Composite composite = sn.getComposite();
					if (composite != null) {
						print("COMPOSITE: " + composite, level + 1);
						if (composite instanceof AlphaComposite) {
							AlphaComposite ac = (AlphaComposite) composite;
							float alpha = ac.getAlpha();
							print("alpha: " + alpha, level + 1);
						}
						g2d.setComposite(sn.getComposite());
					}
					ShapePainter shapePainter = sn.getShapePainter();
					if (shapePainter instanceof CompositeShapePainter) {
						AffineTransform transform = sn.getGlobalTransform();
						g2d.setTransform(transform);
						CompositeShapePainter csp = (CompositeShapePainter) shapePainter;
						for (int i = 0; i < csp.getShapePainterCount(); i++) {
							ShapePainter sp = csp.getShapePainter(i);
							if (sp instanceof FillShapePainter) {
								FillShapePainter fsp = (FillShapePainter) sp;
								Paint paint = fsp.getPaint();
								if (paint != null) {
									print("PAINT: " + paint, level + 1);
									g2d.setPaint(paint);
									g2d.fill(sn.getShape());
								}
								if (paint instanceof Color) {
									Color c = (Color) paint;
									print("Color: " + c, level + 1);
								}
							} else if (sp instanceof StrokeShapePainter) {
								StrokeShapePainter ssp = (StrokeShapePainter) sp;
								Paint paint = ssp.getPaint();
								print("PAINT: " + paint, level + 1);
								Stroke stroke = ssp.getStroke();
								print("STROKE: " + stroke, level + 1);
								if (stroke != null && paint != null) {
									g2d.setPaint(paint);
									g2d.setStroke(stroke);
									g2d.draw(sn.getShape());
								}
							}
						}
					}
				}
			}
		}
	}

	private static void print(String string, int level)
	{
		for (int i = 0; i < level; i++) {
			System.out.print("  ");
		}
		System.out.println(string);
	}
}