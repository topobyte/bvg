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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.topobyte.bvg.path.Close;
import de.topobyte.bvg.path.CompactPath;
import de.topobyte.bvg.path.CubicTo;
import de.topobyte.bvg.path.LineTo;
import de.topobyte.bvg.path.MoveTo;
import de.topobyte.bvg.path.Path;
import de.topobyte.bvg.path.PathElement;
import de.topobyte.bvg.path.QuadTo;
import de.topobyte.bvg.path.Type;

public class BvgWriter
{

	private BvgImage image;
	private boolean compress;
	private EncodingStrategy strategy;

	public BvgWriter(BvgImage image, boolean compress, EncodingStrategy strategy)
	{
		this.image = image;
		this.compress = compress;
		this.strategy = strategy;
	}

	public void write(File file) throws IOException
	{
		OutputStream os = new FileOutputStream(file);
		write(os);
		os.close();
	}

	public void write(OutputStream os) throws IOException
	{
		BvgOutputStream bvgOutput;
		switch (strategy) {
		default:
		case STRATEGY_DOUBLE:
			bvgOutput = new BvgOutputStreamDouble(os, compress,
					image.getWidth(), image.getHeight());
			break;
		case STRATEGY_INTEGER_DELTA:
			bvgOutput = new BvgOutputStreamIntegerDelta(os, compress,
					image.getWidth(), image.getHeight());
			break;
		}

		List<PaintElement> paintElements = image.getPaintElements();
		List<CompactPath> paths = image.getPaths();
		for (int i = 0; i < paintElements.size(); i++) {
			PaintElement element = paintElements.get(i);
			CompactPath compactPath = paths.get(i);
			Path path = convertPath(compactPath);
			if (element instanceof Stroke) {
				Stroke stroke = (Stroke) element;
				bvgOutput.stroke(stroke, path);
			} else if (element instanceof Fill) {
				Fill fill = (Fill) element;
				bvgOutput.fill(fill, path);
			}
		}
		bvgOutput.close();
	}

	private Path convertPath(CompactPath compactPath)
	{
		List<PathElement> elements = new ArrayList<PathElement>();
		List<Type> types = compactPath.getTypes();
		double[] values = compactPath.getValues();
		int k = 0;
		for (Type type : types) {
			switch (type) {
			case MOVE:
				elements.add(new MoveTo(values[k++], values[k++]));
				break;
			case CLOSE:
				elements.add(new Close());
				break;
			case LINE:
				elements.add(new LineTo(values[k++], values[k++]));
				break;
			case QUAD:
				elements.add(new QuadTo(values[k++], values[k++], values[k++],
						values[k++]));
				break;
			case CUBIC:
				elements.add(new CubicTo(values[k++], values[k++], values[k++],
						values[k++], values[k++], values[k++]));
				break;
			default:
				break;
			}
		}
		return new Path(types, elements);
	}
}
