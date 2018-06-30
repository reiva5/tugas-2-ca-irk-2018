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
		i = len(fileName)-1
		while (fileName[i] != '.'):
			i -= 1
		fileName = fileName[:i]
		fileName += '.irk'
		f = open(fileName, 'wb')
		f.write(tmp.toBytes())
		f.close()
		print("Ukuran hasil kompresi adalah: %ld byte(s)" % sys.getsizeof(tmp))
		print("Eksekusi waktu hasil kompresi adalah: %.3f second(s)" % (end - begin))
	else:
		begin = time.time()
		Compressed.decompress(fileName)
		end = time.time()
print("Eksekusi waktu hasil kompresi adalah: %.3f second(s)" % (end - begin))