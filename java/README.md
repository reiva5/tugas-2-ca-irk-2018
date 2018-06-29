# Tugas 2 Ca-IRK 2018

	Nama : Tony
	NIM  : 13516010
	
# Algoritma dan Implementasi

Algoritma yang digunakan dalam program kompresi ini adalah algoritma Huffman Coding. 

# Analisis Kompleksistas

## Fungsi doCompress

	Variable :
	N : Banyak data dalam byte
	S : Panjang nama file
	M : banyak karakter byte berbeda pada data

1. Melakukan loop pada nama file untuk mendapatkan ekstensi.<br>Kompleksistas : `O(S)`
2. Menghitung frekuensi data dan menyimpannya dalam hash map.<br>Kompleksistas : `amortized O(N)`
3. Membentuk pohon huffman menggunakan library dasar PriorityQueue.<br>Kompleksitas : `O(M logM)`
4. Menyimpan semua code tiap karakter byte pada hashmap dengan melakukan transversal dfs pada pohon huffman.<br>Kompleksitas `O(M)`
5. Melakukan output kode data dari hashmap tiap byte ke buffer.<br>Kompleksistas : `amortized O(N)`

Kompleksistas total : `O(S + N + M logM)`

## Prosedur decompress

	Variable :
	N : Banyak data dalam bit

1. Membaca ekstensi file awal.
2. Membaca pohon huffman.
3. Membaca banyak bytes semula.
4. Membaca data file semula.

Kompleksistas total : `O(N)`