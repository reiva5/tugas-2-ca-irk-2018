import os

class Compressed:
	data = bytearray()
	extension = ""

	def __init__(self):
		print("constructor")
	def toBytes(self):
		return self.data

	def set_extension(self, fileName):
		_, self.extension = os.path.splitext(fileName)
		self.data = str.encode(self.extension) + b'\00'
		# print(self.data)

	def get_RLE_Encoding(self, value, length):
		result = bytearray()

		if(length <= 0):
			result = b''
		elif(length == 1):
			result = value
		elif(length >= 2):
			while (length > 255):
				result = value + value + bytes([255])
				length -= 255
			if(length == 1):
				result += value
			elif(length >= 2):
				result += value + value + bytes([length])
			# print (int.from_bytes(bytes([length]), byteorder='little'))

		return result

	def c_compress(self, f_bytes):
		prev_byte = {"value": b'', "length": 0}
		
		curr_byte = f_bytes.read(1)
		while curr_byte:
			if (curr_byte != prev_byte["value"]):
				self.data += self.get_RLE_Encoding(prev_byte["value"], prev_byte["length"])
				prev_byte["length"] = 1
			else:
				prev_byte["length"] += 1

			prev_byte["value"] = curr_byte
			curr_byte = f_bytes.read(1)

		self.data += self.get_RLE_Encoding(prev_byte["value"], prev_byte["length"])

		print(self.data)

	def c_decompress(self, f_bytes, f_output):
		prev_byte = b''
		counting = False

		curr_byte = f_bytes.read(1)
		while curr_byte:
			if (not counting):
				if (prev_byte != curr_byte):
					prev_byte = curr_byte
				else:
					counting = True
				f_output.write(curr_byte)

			else:
				count = int.from_bytes(curr_byte, byteorder='little') - 2
				for _ in range(count):
					f_output.write(prev_byte)
				prev_byte = b''
				counting = False

			curr_byte = f_bytes.read(1)
		# print(self.data)

def doCompress(namaFile):
	compressed = Compressed()
	compressed.set_extension(namaFile)

	with open(namaFile, "rb") as f_bytes:
		compressed.c_compress(f_bytes)
	
	return compressed

def decompress(namaFile):
	compressed = Compressed()
	compressed.set_extension(namaFile)

	with open(namaFile, "rb") as f_bytes:
		if (compressed.extension != ".irk"):
			print ("Invalid file input!")
		else:
			i = len(namaFile) - 1
			while (namaFile[i] != '.'):
				i -= 1
			namaFileOutput = namaFile[:i]

			cc = f_bytes.read(1)
			while(cc != b'\00'):
				namaFileOutput += cc.decode()
				cc = f_bytes.read(1)
			# print(namaFileOutput)

			with open(namaFileOutput, "wb") as f_output:
				compressed.c_decompress(f_bytes, f_output)
	
	return compressed
