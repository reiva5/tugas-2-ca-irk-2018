#!/usr/bin/python
from operator import itemgetter
import os, sys, struct
nama = ''
result = ''
class HuffmanTree:
    def __init__(self, value, frquency):
        self.value = value
        self.frequency = frquency
        self.left = None
        self.right = None   
    def isTreeEmpty(self):
        return self.value == None
    def isTreeOneElement(self):
        return self.value != None and self.left == None and self.right == None
    def addNode(self, value1, frequency1, value2, frequency2):
        if self.isTreeOneElement():
			# making new nodes in Huffman Tree
            self.left = HuffmanTree(value1,frequency1)
            self.right = HuffmanTree(value2, frequency2)
        elif value1 in self.left.value :
			# find another node in left Huffman subtree
            self.left.addNode(value1, frequency1, value2, frequency2)
        elif value1 in self.right.value :
			# find another node in right Huffman subtree
            self.right.addNode(value1, frequency1, value2, frequency2)
    def encode(self, num, huffman_codes):
        if not self.isTreeOneElement():
			# 0 is written when find another node in left of Huffman Tree while 1 in right of Huffman Tree
            self.left.encode(num+'0', huffman_codes)
            self.right.encode(num+'1', huffman_codes)
        else:
			# reach leaf of Huffman Tree
            temp = []
            temp.append(self.value)
            temp.append(num)
            huffman_codes.append(temp)
    def printTree(self):
		# Checking wheter the Huffman Tree is right
        if self.isTreeOneElement():
            print(self.value)
            print(self.frequency)
        else:
            print(self.value)
            print(self.frequency)
            self.left.printTree()
            self.right.printTree()
class Compressed:
	def __init__(self):
		print("Welcome to Compressor and Decompressor!")
	def toBytes(self):
		global result
		extra_padding = 8 - len(result) % 8
		for i in range(extra_padding):
			result += "0" # adding digits to become multiple of eight 
		padded_info = "{0:08b}".format(extra_padding)
		result = padded_info + result
		# storing in bytes
		b = bytearray()
		for i in range(0, len(result), 8):
			byte = result[i:i+8]
			b.append(int(byte, 2))
		return b
			
def counting(string, array_of_chars):
	for i in range (len(string)):
		adder(string, i, array_of_chars)

def adder(string, i, array_of_chars):
	# Getting all the alphabets used in the input and the frequency
	find=False
	for list_element in array_of_chars:
		if list_element[0]==string[i]:
			list_element[1]+=1
			find=True
			break
	if(find==False):
		element = []
		element.append(string[i])
		element.append(1)
		array_of_chars.append(element)
def making_path(copy_of_array_of_chars,path_insertion_to_huffman_tree):
	#Making path as [[left_char, left_char_frequency], [right_char, right_char_frequency]]
	for i in range(len(copy_of_array_of_chars)-1) :
		string = ""
		string = copy_of_array_of_chars[0][0]+copy_of_array_of_chars[1][0]
		total = 0
		total = copy_of_array_of_chars[0][1]+copy_of_array_of_chars[1][1]
		new_element=[]
		new_element.append(string)
		new_element.append(total)
		temp = []
		temp.append (copy_of_array_of_chars[0])
		temp.append (copy_of_array_of_chars[1])
		path_insertion_to_huffman_tree.append(temp)
		del copy_of_array_of_chars[0:2]
		j = 0
		if len(copy_of_array_of_chars)!=0:
			while total >= copy_of_array_of_chars[j][1]:
				j+=1
				if(j==len(copy_of_array_of_chars)): break
		else:
			temp = []
			temp.append (new_element)
			path_insertion_to_huffman_tree.append(temp)
		copy_of_array_of_chars.insert(j,new_element)
	path_insertion_to_huffman_tree.reverse()

def compress(huffman_codes, line):
	string_result= ''
	for string in line:
		for i in range(len(string)):
			binary = ''
			char = string[i]
			for map_binary in huffman_codes:
				if char == map_binary[0]:
					binary = map_binary[1]
					break
			string_result += binary
	return string_result
	
def convertIntoByte(result):
	extra_padding = 8 - len(result) % 8
	for i in range(extra_padding):
		result += "0"
	padded_info = "{0:08b}".format(extra_padding)
	result = padded_info + result
	b = bytearray()
	for i in range(0, len(result), 8):
		byte = result[i:i+8]
		b.append(int(byte, 2))
	return b

def doCompress(fileName):
	global nama
	global result
	extension = os.path.splitext(os.path.basename(fileName))[1]
	if(extension == ".txt"):
		with open(fileName, "r+",encoding='utf-8-sig') as file_object:
			line = file_object.readlines()
			file_object.seek(0)
			array_of_chars = []
			for string in line:
				counting(string, array_of_chars)
			array_of_chars.sort(key=itemgetter(1))
			copy_of_array_of_chars = array_of_chars.copy()
			path_insertion_to_huffman_tree = []
			making_path(copy_of_array_of_chars,path_insertion_to_huffman_tree)
			compression_huffman_tree = HuffmanTree(path_insertion_to_huffman_tree[0][0][0],path_insertion_to_huffman_tree[0][0][1])
			for i in range (len(path_insertion_to_huffman_tree)-1):
				compression_huffman_tree.addNode(path_insertion_to_huffman_tree[i+1][0][0],path_insertion_to_huffman_tree[i+1][0][1], path_insertion_to_huffman_tree[i+1][1][0],path_insertion_to_huffman_tree[i+1][1][1])
			num = ''
			huffman_codes = []
			compression_huffman_tree.encode(num, huffman_codes)
			file_huffman_path = open("huffman_encoding_text.txt", "w")
			for item in huffman_codes:
				file_huffman_path.write("%s %s\n" %(item[0],item[1]))
			file_huffman_path.close()
			result = compress(huffman_codes, line)
			file_object.close()
			nama = fileName
			os.remove(fileName)
		return Compressed() # since the result in doCompress is an instance of class Compressed
	else:
		print("Wrong file extension. File extension must be '.txt'")
		return Compressed() # since the result in doCompress is an instance of class Compressed
	
def remove_padding(bit_string):
	additional_bits = bit_string[:8] # additional_bits never exceed 8 bits due to saving with bytes form
	extra_bits = int(additional_bits, 2)
	text = bit_string[8:] 
	encoded_text = text[:-1*extra_bits]
	return encoded_text

def readinghuffman(char_to_bit_decode):
	# to make the file become huffman_code in doCompress 
	file = open("huffman_encoding_text.txt", "r", encoding='utf-8-sig')
	line = file.readlines()
	find_enter = False
	i = 0
	j = 0
	for j in range (len(line)):
		if line[j] == '\n':
			temp_array = []
			i = j
			find_enter = True
			temp_array.append (line[j])
			temp_array.append (line[j+1][1:len(line[j+1])-1])
			char_to_bit_decode.append(temp_array)
		elif line[j][0] == ' ':
			temp_array = []
			temp_array.append (line[j][:1])
			temp_array.append (line[j][2:len(line[j])-1])
			char_to_bit_decode.append(temp_array)
		else:
			char_to_bit_decode.append(line[j].split())
		j += 1
	if find_enter:
		del char_to_bit_decode[i+1:i+2]
	file.close()
	
	os.remove("huffman_encoding_text.txt") 

def finding_pattern(string, encoding):
	i = 0
	for i in range(len(encoding)):
		if(string == encoding[i][1]):
			return i
	return -1
			
def change(index, encoding_decoding, char_to_bit_decode):
	encoding_decoding[0] = encoding_decoding[0][len(char_to_bit_decode[index][1]):]
	encoding_decoding[1] = encoding_decoding[1] + char_to_bit_decode[index][0]
	
def finding_char(char, char_to_bit_decode):
	i = 0
	for i in range (len(char_to_bit_decode)):
		if char_to_bit_decode[i] == char:
			return i

def returning(encoding_decoding, char_to_bit_decode):
	# return to previous encode_text and decode_text 
	returning_char = encoding_decoding[1][-1]
	encoding_decoding[1] = encoding_decoding[1][0:len(encoding_decoding[1])-1]
	index = finding_char(returning_char, char_to_bit_decode)
	encoding_decoding[0] = char_to_bit_decode[index][0] + encoding_decoding[0]
	return index
	
def decoding(starting_index, encoding_decoding, char_to_bit_decode, never_change):
	if(len(encoding_decoding[0]) == 0):
		# Finished decoding
		return encoding_decoding[1]
	elif(starting_index == len(char_to_bit_decode)) and (not (never_change)):
		# It's false. Must trackback
		starting_index = returning(encoding_decoding, char_to_bit_decode)
		decoding(starting_index, encoding_decoding, char_to_bit_decode, never_change)
	else:
		index = finding_pattern(encoding_decoding[0][:len(char_to_bit_decode[starting_index][1])], char_to_bit_decode)
		if(index != -1):
			# the char doesn't match, find another
			change(index, encoding_decoding, char_to_bit_decode)
			starting_index = 0
			never_change = True
			decoding(starting_index, encoding_decoding, char_to_bit_decode, never_change)
		else: # not find any char that matches, must finding another string that matches
			starting_index += 1
			if starting_index == len(char_to_bit_decode) - 1:
				never_change = False
			decoding(starting_index, encoding_decoding, char_to_bit_decode, never_change)
			
def decompress(fileName):
	max_int = 2 ** (struct.Struct('i').size * 8 - 1) - 1
	sys.setrecursionlimit(max_int) # with maximum recursion  limit, cannot prevent stack overflow
	extension = os.path.splitext(os.path.basename(fileName))[1]
	if(extension == ".irk"):
		with open(fileName,"rb") as file_object:
			char_to_bit_decode = []
			readinghuffman(char_to_bit_decode)
			bit_string = ""
			#transform byte into string of bits, still contains padded_info
			byte = file_object.read(1)
			while(len(byte)!=0):
				byte = ord(byte)
				bits = bin(byte)[2:].rjust(8, '0')
				bit_string += bits
				byte = file_object.read(1)
			encoded_text = remove_padding(bit_string)
			never_change = False
			starting_index = 0
			encoding_decoding = []
			decode_text = ''
			encoding_decoding.append(encoded_text)
			encoding_decoding.append(decode_text)
			decoding(starting_index, encoding_decoding, char_to_bit_decode, never_change)
			file_object.close()
			new_file_name = os.path.splitext(os.path.basename(fileName))[1]+".txt"
		with open(new_file_name, "w+") as file_object:
			file_object.write(encoding_decoding[1])
			file_object.truncate()
			file_object.close()
	else:
print("Wrong file extension. File extension must be '.irk'")
