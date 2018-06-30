class Compressed:
	# Sebuah kelas dengan 2 member variabel s dan b
	# dengan s adalah sebuah string
	# dan b adalah representasi binary dari s
	# def __init__(self, t_string = ""):
	def __init__(self, t_binary = b""):
		# self.s = t_string
		self.b = t_binary
		print("done")
	def toBytes(self):
		return self.b
	# YOU CAN ADD ANOTHER METHOD OR VARIABLE HERET

from math import ceil
MapASCII = dict({i.to_bytes(1, "big") : i for i in range(256)})
MapINT = dict({v : k for k, v in MapASCII.items()})

def doCompress(namaFile):
	with open(namaFile, "r") as f:
		data = f.read()
		f.close()
	print(data)

	# if isinstance(data, str):
	data = data.encode()
	keys: dict = MapASCII.copy()
	n_keys: int = 256
	compressed: list = []
	start: int = 0
	n_data: int = len(data)+1
	while True:
		if n_keys >= 512:
			# print("MASUK SINI BGST")
			keys = MapASCII.copy()
			n_keys = 256
		# print("TERNYATA MASUK SINI BGST")
		for i in range(1, n_data-start):
			w: bytes = data[start:start+i]
			if w not in keys:
				compressed.append(keys[w[:-1]])
				keys[w] = n_keys
				start += i-1
				n_keys += 1
				break
		else:
			compressed.append(keys[w])
			break
	t_string = str = ''.join([bin(i)[2:].zfill(9) for i in compressed])
	t_binary = int(t_string, 2).to_bytes(ceil(len(t_string) / 8), 'big')

	print(t_string)
	print(t_string.encode())
	print(t_binary)
	print(t_binary.decode())

	return "pass"

def decompress(namaFile):
	return "suasu"
