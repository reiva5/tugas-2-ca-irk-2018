import os
import sys
import time
from Compressed import *

argv = sys.argv
filename = argv[1]
mode = argv[2]

start_time = time.time()

if mode=="1":
    doCompress(filename)
elif mode=="2":
    decompress(filename)

duration = time.time() - start_time
print("Time needed : {:.4f} seconds".format(duration))
