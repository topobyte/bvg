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

package de.topobyte.bvg;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import de.topobyte.bvg.path.CompactPath;

public class BvgAndroidPainter
{
	public static void draw(Canvas canvas, BvgImage bvg, float x, float y,
			float sx, float sy)
	{
		canvas.save();

		canvas.translate(x, y);
		canvas.scale(sx, sy);

		List<PaintElement> elements = bvg.getPaintElements();
		List<CompactPath> paths = bvg.getPaths();

		Paint pFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		pFill.setStyle(Paint.Style.FILL);

		Paint pStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		pStroke.setStyle(Paint.Style.STROKE);

		for (int i = 0; i < elements.size(); i++) {
			PaintElement element = elements.get(i);
			CompactPath path = paths.get(i);

			Path p = ToAndroidUtil.createPath(path);

			if (element instanceof Fill) {
				Fill fill = (Fill) element;
				IColor color = fill.getColor();

				int c = color.getColorCode();
				pFill.setColor(c);

				canvas.drawPath(p, pFill);
			} else if (element instanceof Stroke) {
				Stroke stroke = (Stroke) element;

				IColor color = stroke.getColor();
				int c = color.getColorCode();
				pStroke.setColor(c);

				LineStyle lineStyle = stroke.getLineStyle();
				Cap cap = ToAndroidUtil.getCap(lineStyle.getCap());
				Join join = ToAndroidUtil.getJoin(lineStyle.getJoin());
				float[] dashArray = lineStyle.getDashArray();
				float dashOffset = lineStyle.getDashOffset();

				pStroke.setStrokeWidth(lineStyle.getWidth());
				pStroke.setStrokeCap(cap);
				pStroke.setStrokeJoin(join);
				pStroke.setStrokeMiter(lineStyle.getMiterLimit());

				if (dashArray != null) {
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
