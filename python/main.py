import Compressed
import sys
import time

if  __name__ == "__main__":
	fileName = sys.argv[1]
	query = int(sys.argv[2])
	tmp = Compressed.Compressed()
	if (query == 1):
		begin = time.time()
		tmp = Compressed.doCompress(fileName)
		end = time.time()
		print("Ukuran hasil kompresi adalah: %ld byte(s)" % sys.getsizeof(tmp))
		print("Eksekusi waktu hasil kompresi adalah: %.3f second(s)" % (end - begin))
	else:
		begin = time.time()
		Compressed.decompress(fileName)
		end = time.time()
		print("Eksekusi waktu hasil kompresi adalah: %.3f second(s)" % (end - begin))