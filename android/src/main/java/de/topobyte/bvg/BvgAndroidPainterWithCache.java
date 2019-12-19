// Copyright 2015 Sebastian Kuerten
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

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import de.topobyte.bvg.android.cache.ColorPath;
import de.topobyte.bvg.path.CompactPath;

public class BvgAndroidPainterWithCache
{

	private final List<PaintElement> elements;
	private final List<ColorPath> ps = new ArrayList<>();

	public BvgAndroidPainterWithCache(BvgImage bvg)
	{
		elements = bvg.getPaintElements();
		List<CompactPath> paths = bvg.getPaths();

		for (int i = 0; i < elements.size(); i++) {
			PaintElement element = elements.get(i);
			CompactPath path = paths.get(i);

			Path p = ToAndroidUtil.createPath(path);

			if (element instanceof Fill) {
				Fill fill = (Fill) element;
				IColor color = fill.getColor();

				int c = color.getColorCode();
				ps.add(new ColorPath(p, c));
			} else if (element instanceof Stroke) {
				Stroke stroke = (Stroke) element;

				IColor color = stroke.getColor();
				int c = color.getColorCode();
				ps.add(new ColorPath(p, c));
			}
		}
	}

	public void draw(Canvas canvas, float x, float y, float sx, float sy,
			float sw)
	{
		canvas.save();

		Paint pFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		pFill.setStyle(Paint.Style.FILL);

		Paint pStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		pStroke.setStyle(Paint.Style.STROKE);

		Matrix m = new Matrix();
		m.postScale(sx, sy);
		m.postTranslate(x, y);

		Path p = new Path();

		for (int i = 0; i < elements.size(); i++) {
			PaintElement element = elements.get(i);
			ColorPath colorPath = ps.get(i);

			p.reset();
			colorPath.getPath().transform(m, p);

			if (element instanceof Fill) {
				pFill.setColor(colorPath.getColor());

				canvas.drawPath(p, pFill);
			} else if (element instanceof Stroke) {
				Stroke stroke = (Stroke) element;
				pStroke.setColor(colorPath.getColor());

				LineStyle lineStyle = stroke.getLineStyle();
				Cap cap = ToAndroidUtil.getCap(lineStyle.getCap());
				Join join = ToAndroidUtil.getJoin(lineStyle.getJoin());
				float[] dashArray = lineStyle.getDashArray();
				float dashOffset = lineStyle.getDashOffset();

				pStroke.setStrokeWidth(lineStyle.getWidth() * sw);
				pStroke.setStrokeCap(cap);
				pStroke.setStrokeJoin(join);
				pStroke.setStrokeMiter(lineStyle.getMiterLimit());

				if (dashArray == null) {
					pStroke.setPathEffect(null);
				} else {
					DashPathEffect effect = new DashPathEffect(dashArray,
							dashOffset);
					pStroke.setPathEffect(effect);
				}

				canvas.drawPath(p, pStroke);
			}
		}

		canvas.restore();
	}

}
