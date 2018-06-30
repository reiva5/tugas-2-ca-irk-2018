# ============================
# (c) Ihsan M. A. / 135-16-028
# ============================
from math import ceil
# ============================

totalKeys = 256
SPACE = " "
MapASCII = dict({i.to_bytes(1, "big") : i for i in range(totalKeys)})
MapINT = dict({v : k for k, v in MapASCII.items()})

class Compressed:

	# Data holds bytes array
	# Ext holds file extension
	def __init__(self):
		self.data = b""
		self.ext = ""

	def getExt(self):
		return self.ext
	def getData(self):
		return self.data

	def setExt(self, ext):
		self.ext = ext
	def setData(self, data):
		self.data = data
	
	def toBytes(self):
		return self.getData()

	def compressLZW(self, fileName):
		# Get extension from original file
		self.setExt(Compressed.__getFileExtension(fileName))

		# Initialize data with extension 
		data = self.getExt() + SPACE

		# Get data from file
		data += Compressed.__readString(fileName)
		
		# Encode string to bytes
		data = Compressed.__encodeToBytes(data)

		dataCompressed = []
		table = MapASCII.copy()
		start = 0
		lengthOfData = len(data)+1
		keys = totalKeys

		while 1:
			if keys >= 512:
				table = MapASCII.copy()
				keys = 256

			for i in range(1, lengthOfData-start):
				# Find longest string W in dictionary that matches character input
				currString = data[start:start+i]

				if currString not in table:
					# Output value of W 
					dataCompressed.append(table[currString[:-1]])

					# Add W + new charater to dictionary
					table[currString] = keys

					# Remove W from stream
					start += i-1

					keys += 1
					break
			else:
				# W is the last string in stream and W in table
				dataCompressed.append(table[currString])
				break

		# Convert every element in dataCompressed to binary and join in 1 string
		t_string = ''.join([bin(i)[2:].zfill(9) for i in dataCompressed])
		# Convert string of binary to integer, then convert to bytes array with padding bits
		t_binary = int(t_string, 2).to_bytes(ceil(len(t_string) / 8), 'big')

		self.setData(t_binary)

	def decompressLZW(self, fileName):
		# Get data from file
		data = Compressed.__readBytes(fileName)

		table = MapINT.copy()

		# Convert bytes array to integer, then convert to binary and add padding bits
		bits = str(bin(int.from_bytes(data, 'big'))[2:].zfill(len(data) * 8))
		
		extendedBytes = len(bits) // 9

		# Remove unnecessary padding bits
		bits = bits[-extendedBytes * 9:]

		# Split and convert binary to integer
		undecoded = [int(bits[i*9:(i+1)*9], 2) for i in range(extendedBytes)]

		# Map integer to ASCII
		prev = table[undecoded[0]]
		uncompressed = [prev]
		keys = 256

		for num in undecoded[1:]:
			if keys >= 512:
				table = MapINT.copy()
				keys = 256

			try:
				current = table[num]
			except KeyError:
				current = prev + prev[:1]

			uncompressed.append(current)
			table[keys] = prev + current[:1]
			prev = current
			keys += 1

		# Merge bytes array and decode
		result = b"".join(uncompressed).decode()

		# Getting extension from decoded string
		ext = Compressed.__extractExtension(result)
		self.setExt(ext)

		# Cut extension from decoded string
		result = result[len(ext)+1:]

		# Decide on output file name
		outputFile = Compressed.__getOutputFileName(fileName, self.getExt())

		Compressed.__writeString(outputFile, result)

	def __getOutputFileName(inputFileName, desiredExt):
		return inputFileName[:inputFileName.index(".")] + "." + desiredExt

	# Extract extension from decoded string (extension is placed before the actual text)
	def __extractExtension(tString):
		return tString[:tString.index(" ")]

	def __getFileExtension(fileName):
		return fileName[fileName.index(".")+1:]

	def __encodeToBytes(string):
		return string.encode()

	def __readBytes(fileName):
		return Compressed.__inputFromFile(fileName, "rb")

	def __readString(fileName):
		return Compressed.__inputFromFile(fileName, "r")

	def __writeBytes(fileName, data):
		Compressed.__outputToFile(fileName, data, "wb")

	def __writeString(fileName, data):
		Compressed.__outputToFile(fileName, data, "w")

	def __inputFromFile(fileName, option):
		with open(fileName, option) as f:
			data = f.read()
			f.close()
		return data

	def __outputToFile(fileName, data, option):
		with open(fileName, option) as f:
			f.write(data)
			f.close()

def doCompress(namaFile):
	t = Compressed()
	t.compressLZW(namaFile)
	return t

def decompress(namaFile):
	t = Compressed()
	t.decompressLZW(namaFile)
	



