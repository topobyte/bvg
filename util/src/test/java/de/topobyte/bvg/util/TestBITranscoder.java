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

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.batik.transcoder.TranscoderInput;

public class TestBITranscoder
{
	public static void main(String[] args) throws Exception
	{
		String input = "/home/z/git/map-icons/test/atm.svg";
		String output = "/home/z/git/map-icons/test/atm.bi.png";

		BufferedImageTranscoder t = new BufferedImageTranscoder();
		String svgURI = new File(input).toURI().toString();
		TranscoderInput tin = new TranscoderInput(svgURI);
		t.transcode(tin, null);

		BufferedImage image = t.getBufferedImage();
		ImageIO.write(image, "png", new File(output));
	}
}
