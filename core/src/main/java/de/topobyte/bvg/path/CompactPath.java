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

package de.topobyte.bvg.path;

import java.util.List;

public class CompactPath
{

	private final FillRule fillRule;
	private final List<Type> types;
	private final double[] values;

	public CompactPath(FillRule fillRule, List<Type> types, double[] values)
	{
		this.fillRule = fillRule;
		this.types = types;
		this.values = values;
	}

	public FillRule getFillRule()
	{
		return fillRule;
	}

	public List<Type> getTypes()
	{
		return types;
	}

	public double[] getValues()
	{
		return values;
	}

}
