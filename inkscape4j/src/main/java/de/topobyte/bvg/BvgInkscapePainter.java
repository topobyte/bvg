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

package de.topobyte.bvg;

import static de.topobyte.inkscape4j.Styles.style;

import java.util.List;

import de.topobyte.bvg.path.CompactPath;
import de.topobyte.chromaticity.ColorCode;
import de.topobyte.inkscape4j.Layer;
import de.topobyte.inkscape4j.ids.IdFactory;
import de.topobyte.inkscape4j.path.Path;
import de.topobyte.inkscape4j.style.LineCap;
import de.topobyte.inkscape4j.style.LineJoin;

public class BvgInkscapePainter
{

	public static void draw(IdFactory idFactory, Layer layer, BvgImage bvg,
			float x, float y, float sx, float sy)
	{
		List<PaintElement> elements = bvg.getPaintElements();
		List<CompactPath> paths = bvg.getPaths();

		for (int i = 0; i < elements.size(); i++) {
			PaintElement element = elements.get(i);
			CompactPath path = paths.get(i);

			Path p = ToInkscapeUtil.createPath(idFactory, path);

			if (element instanceof Fill) {
				Fill fill = (Fill) element;

				IColor color = fill.getColor();
				ColorCode c = new ColorCode(color.getRed(), color.getGreen(),
						color.getBlue(), color.getAlpha());
				int alpha = c.getAlpha();

				p.setStyle(style(c, null, alpha / 255.0, 1, 1, 0));
				layer.getObjects().add(p);
			} else if (element instanceof Stroke) {
				Stroke stroke = (Stroke) element;

				IColor color = stroke.getColor();
				ColorCode c = new ColorCode(color.getRed(), color.getGreen(),
						color.getBlue(), color.getAlpha());
				int alpha = c.getAlpha();

				LineStyle lineStyle = stroke.getLineStyle();
				LineCap cap = ToInkscapeUtil.getCap(lineStyle.getCap());
				LineJoin join = ToInkscapeUtil.getJoin(lineStyle.getJoin());
				float[] dashArray = lineStyle.getDashArray();
				float dashOffset = lineStyle.getDashOffset();

				p.setStyle(style(null, c, alpha / 255.0, 1, 1,
						lineStyle.getWidth()));
				p.getStyle().setLineCap(cap);
				p.getStyle().setLineJoin(join);

				if (dashArray != null) {
					p.getStyle().setDashArray(dashArray);
					p.getStyle().setDashOffset(dashOffset);
				}
			}
		}
	}

}
