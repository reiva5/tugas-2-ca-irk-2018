class Compressed:
	words_init = []
	words_count = []
	converted = []
	dictionary = {}
	words_final = []
	
	def __init__(self):
		print("Mulai")

	def toBytes(self):
		word_compressed = []
		for word in self.words_init:
			biner = self.dictionary[word].to_bytes(2, byteorder='big')
			word_compressed.append(biner)
			word_compressed.append(b" ")

	def getKey(self, item):
		return item[0]

	def doCompress(self, namaFile):
		cmp = Compressed()
		with open(namaFile, "r") as f_write:
			for line in f_write:
				cmp.words_init += line.split() 
				cmp.words_init.append('\n')
		cmp.words_count = [(self.words_init.count(x), x) for x in set(self.words_init)]
		cmp.words_count.sort(key=self.getKey, reverse=True)
		
		counter = 1
		for item in self.words_count:
			self.dictionary.update({item[1]:counter})
			counter += 1

		f_write.close()

		return(cmp)

	def decompress(self, namaFile):
		with open(namaFile, "rb") as f_read:
			converted = (f_read.readline()).split()

			for word in converted:
				number = int.from_bytes(word, byteorder='big')
				for k, v in self.dictionary.items():
					if number==v:
						self.words_final.append(k)
		
		i = len(namaFile)-1
		while (namaFile[i] != '.'):
			i -= 1
		namaFile = namaFile[:i]
		namaFile += '.txt'
		with open(namaFile, 'w') as f_final:
			for word in self.words_final:
				if word=="\n":
					f_final.write('\n')
				else :
					f_final.write(word)
					f_final.write(' ')