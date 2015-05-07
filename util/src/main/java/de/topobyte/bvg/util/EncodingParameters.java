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

package de.topobyte.bvg.util;

import de.topobyte.bvg.EncodingMethod;
import de.topobyte.bvg.EncodingStrategy;

public class EncodingParameters
{

	private EncodingMethod method;
	private EncodingStrategy strategy;

	public EncodingParameters(EncodingMethod method, EncodingStrategy strategy)
	{
		this.method = method;
		this.strategy = strategy;
	}

	public EncodingMethod getMethod()
	{
		return method;
	}

	public EncodingStrategy getStrategy()
	{
		return strategy;
	}

	public void setMethod(EncodingMethod method)
	{
		this.method = method;
	}

	public void setStrategy(EncodingStrategy strategy)
	{
		this.strategy = strategy;
	}

}
