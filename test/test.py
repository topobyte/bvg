#!/usr/bin/python3

import sys
import os
import shlex
import subprocess
import tempfile
import shutil

METHODS = ["plain", "deflate", "lz4"]
STRATEGIES = ["double", "int_delta"]


def create_images(svg, dir_bvgs, dir_pngs, name):
    for m in range(len(METHODS)):
        method = METHODS[m]
        for s in range(len(STRATEGIES)):
            strategy = STRATEGIES[s]
            bvg = os.path.join(dir_bvgs, name + "." + strategy + "." + method + ".bvg")
            png = os.path.join(dir_pngs, name + "." + strategy + "." + method + ".png")
            print(bvg)
            create_bvg(svg, bvg, method, strategy)
            create_png(bvg, png)


def create_bvg(svg, bvg, method, strategy):
    cmd = "SvgToBvg -compress " + method + " -strategy " + strategy \
          + " " + svg + " " + bvg
    print(cmd)
    args = shlex.split(cmd, False, True)
    subprocess.call(args)


def create_png(bvg, png):
    cmd = "BvgToPng " + bvg + " " + png
    print(cmd)
    args = shlex.split(cmd, False, True)
    subprocess.call(args)


if __name__ == "__main__":

    nargs = len(sys.argv)
    if nargs != 4:
        print ("usage: " + sys.argv[0] +
               " <filename (svg input)> <output bvg> <output png>")
        exit(1)

    svg_path = sys.argv[1]
    dir_bvgs = sys.argv[2]
    dir_pngs = sys.argv[3]

    svg_name = os.path.basename(svg_path)
    name = svg_name
    if svg_name.lower().endswith(".svg"):
        name = svg_name[:-4]

    create_images(svg_path, dir_bvgs, dir_pngs, name)
