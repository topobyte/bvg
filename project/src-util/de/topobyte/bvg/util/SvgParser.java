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
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

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

import de.topobyte.bvg.Color;
import de.topobyte.bvg.LineStyle;

public class SvgParser
{

	private ShapeSink sink;

	public SvgParser(ShapeSink sink)
	{
		this.sink = sink;
	}

	public void parseToSink(File file) throws MalformedURLException,
			IOException
	{
		String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory df = new SAXSVGDocumentFactory(xmlParser);
		SVGDocument doc = df.createSVGDocument(file.toURI().toString());
		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(userAgent);
		BridgeContext ctx = new BridgeContext(userAgent, loader);
		ctx.setDynamicState(BridgeContext.DYNAMIC);
		GVTBuilder builder = new GVTBuilder();
		RootGraphicsNode rootGN = (RootGraphicsNode) builder.build(ctx, doc);

		Dimension2D docSize = ctx.getDocumentSize();
		double width = docSize.getWidth();
		double height = docSize.getHeight();

		sink.init(width, height);

		go(rootGN, 0);
	}

	private void go(CompositeGraphicsNode cgn, int level) throws IOException
	{
		print(cgn.toString(), level);
		List<?> children = cgn.getChildren();
		for (Object child : children) {
			if (child instanceof CompositeGraphicsNode) {
				go((CompositeGraphicsNode) child, level + 1);
			} else {
				print("LEAF: " + child.toString(), level);
				if (child instanceof ShapeNode) {
					int alpha = 255;
					ShapeNode sn = (ShapeNode) child;
					Composite composite = sn.getComposite();
					if (composite != null) {
						print("COMPOSITE: " + composite, level + 1);
						if (composite instanceof AlphaComposite) {
							AlphaComposite ac = (AlphaComposite) composite;
							float alphaF = ac.getAlpha();
							alpha = Math.round(alphaF * 255);
							print("alpha: " + alpha, level + 1);
						}
					}

					AffineTransform transform = sn.getGlobalTransform();
					Shape shape = sn.getShape();
					Shape tshape = transform.createTransformedShape(shape);

					ShapePainter shapePainter = sn.getShapePainter();
					if (shapePainter instanceof CompositeShapePainter) {
						CompositeShapePainter csp = (CompositeShapePainter) shapePainter;
						for (int i = 0; i < csp.getShapePainterCount(); i++) {
							ShapePainter sp = csp.getShapePainter(i);
							if (sp instanceof FillShapePainter) {
								print("FILL", level + 1);
								FillShapePainter fsp = (FillShapePainter) sp;
								Paint paint = fsp.getPaint();
								if (paint != null) {
									print("PAINT: " + paint, level + 1);
									if (paint instanceof java.awt.Color) {
										java.awt.Color c = (java.awt.Color) paint;
										print("Color: " + c, level + 1);
										Color color = new Color(c.getRGB(),
												alpha);
										sink.fill(tshape, color);
									}
								}
							} else if (sp instanceof StrokeShapePainter) {
								print("STROKE", level + 1);
								StrokeShapePainter ssp = (StrokeShapePainter) sp;
								Paint paint = ssp.getPaint();
								print("PAINT: " + paint, level + 1);
								Stroke stroke = ssp.getStroke();
								print("STROKE: " + stroke, level + 1);
								if (stroke != null && paint != null) {
									if (paint instanceof java.awt.Color
											&& stroke instanceof BasicStroke) {
										java.awt.Color c = (java.awt.Color) paint;
										Color color = new Color(c.getRGB(),
												alpha);

										BasicStroke bs = (BasicStroke) stroke;
										float width = bs.getLineWidth();
										int endCap = bs.getEndCap();
										int lineJoin = bs.getLineJoin();
										print("Line Width: " + width, level + 1);

										LineStyle lineStyle = new LineStyle(
												width,
												SwingUtil.getCap(endCap),
												SwingUtil.getJoin(lineJoin));
										if (lineJoin == BasicStroke.JOIN_MITER) {
											float miterLimit = bs
													.getMiterLimit();
											lineStyle.setMiterLimit(miterLimit);
										}

										sink.stroke(tshape, color, lineStyle);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void print(String string, int level)
	{
		for (int i = 0; i < level; i++) {
			System.out.print("  ");
		}
		System.out.println(string);
	}

}
