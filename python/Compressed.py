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

def compress(huffman_codes, line, string_helper):
	string_result= ''
	string_encoding = []
	for string in line:
		for i in range(len(string)):
			char = string[i]
			index, binary = find_char(char, huffman_codes)
			string_result += binary
			string_encoding.append(index)
	string_helper.append(string_result)
	string_helper.append(string_encoding)
			
def find_char(char, huffman_codes):
	for j in range(len(huffman_codes)):
		if char == huffman_codes[j][0]:
			binary = huffman_codes[j][1]
			return j, binary

def doCompress(fileName):
	global nama
	extension = os.path.splitext(os.path.basename(fileName))[1]
	if(extension == ".txt"):
		with open(fileName, "r+",encoding='utf-8-sig') as file_object:
			global result
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
			string_helper = []
			compress(huffman_codes, line, string_helper)
			result = string_helper[0]
			string_encoding = string_helper[1]
			# Making a file to "encode and decode" the binary string
			file_huffman_path = open("huffman_encoding_text.txt", "w")
			for item in huffman_codes:
				file_huffman_path.write("%s %s\n" %(item[0],item[1]))
			for int in string_encoding:
				file_huffman_path.write("%s \n" %int)
			file_huffman_path.close()
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
	encoded_text = text[:-1*extra_bits] # obtaining the real binary string
	return encoded_text

def readinghuffman(char_to_bit_decode, list_of_encoding):
	# to make the file become huffman_code and string_encoding variable in doCompress 
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
		elif (' ' not in line[j].rstrip().strip()):
			list_of_encoding.append(int(line[j].rstrip().strip()))
		else:
			char_to_bit_decode.append(line[j].split())
		j += 1
	if find_enter:
		del char_to_bit_decode[i+1:i+2]
	file.close()
	os.remove("huffman_encoding_text.txt") # The file is not needed anymore
	
def decoding(encoding_decoding, char_to_bit_decode, list_of_encoding):
	for length in list_of_encoding:
		encoding_decoding[2] = encoding_decoding[0][:len(char_to_bit_decode[length][1])]
		encoding_decoding[0] = encoding_decoding[0][len(char_to_bit_decode[length][1]):] # To make sure length of encoded text decrement every decoding
		encoding_decoding[1] = encoding_decoding[1] + char_to_bit_decode[length][0]
			
def decompress(fileName):
	extension = os.path.splitext(os.path.basename(fileName))[1]
	if(extension == ".irk"):
		with open(fileName,"rb") as file_object:
			char_to_bit_decode = []
			list_of_encoding = []
			readinghuffman(char_to_bit_decode, list_of_encoding)
			bit_string = ""
			#transform byte into string of bits, still contains padded_info
			byte = file_object.read(1)
			while(len(byte)!=0):
				byte = ord(byte)
				bits = bin(byte)[2:].rjust(8, '0')
				bit_string += bits
				byte = file_object.read(1)
			encoded_text = remove_padding(bit_string)
			encoding_decoding = []
			decode_text = ''
			string = ''
			encoding_decoding.append(encoded_text)
			encoding_decoding.append(decode_text)
			encoding_decoding.append(string)
			decoding(encoding_decoding, char_to_bit_decode, list_of_encoding)
			file_object.close()
		if(encoding_decoding[0] == ''):
			# The decoding is right
			new_file_name = os.path.splitext(os.path.basename(fileName))[1]+".txt"
			with open(new_file_name, "w+") as file_object:
				file_object.write(encoding_decoding[1])
				file_object.truncate()
				file_object.close()
	else:
print("Wrong file extension. File extension must be '.irk'")
