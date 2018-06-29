# Tugas 2 Ca-IRK 2018

	Nama : Tony
	NIM  : 13516010
	
# Algoritma

Algoritma yang digunakan dalam program kompresi ini adalah algoritma Huffman Coding.
Algoritma Huffman adalah algoritma kompresi yang lossless yang menggunakan pendekatan statistik dengan cara melakukan pengkodean dalam bentuk bit 
untuk mewakili data karakter. Huffman Coding menggunakan struktur pohon dalam pemrosesannya.

Saya memilih algoritma ini karena cukup efisien dan banyak software yang menggunakan algoritma ini (baik yang tidak dimodifikasi maupun dimodifikasi) dalam melakukan kompresi.

# Implementasi dan Analisis Kompleksistas

## Pohon Huffman Pada File

### Representasi

1. untuk tiap node yang bukan daun diberi bit 0.
2. untuk tiap node daun diberi bit 1 dan 8 bit yang merupakan nilai data.

### Cara Baca Representasi

1. untuk tiap Node, jika bit 0 yang dibaca, secara rekursif baca Node kiri dan Node kanan.
2. untuk tiap Node, jika bit 1 yang dibaca, baca 8 bit data.

## Representasi Data Hasil Kompresi

Data dari hasil kompresi (yang berupa Bit, Byte, dan Integer) dimampatkan dalam array byte. Implementasi ini terdapat pada kelas OutputArrayData.

## Class InputArrayByte

Merupakan kelas yang digunakan untuk membaca bit perbit data dari array byte hasil kompresi. Pembacaan ini dapat menghasilkan data berupa Bit, Byte, dan Integer.

## Fungsi doCompress

	Variable :
	N : Banyak data dalam byte
	S : Panjang nama file
	M : banyak karakter byte berbeda pada data

1. Melakukan loop pada nama file untuk mendapatkan ekstensi.<br>Kompleksistas : `O(S)`
2. Menghitung frekuensi data dan menyimpannya dalam hash map (kompleksitas hashmap : `amortized O(1)`).<br>Kompleksistas : `O(N)`
3. Membentuk pohon huffman menggunakan library dasar PriorityQueue.<br>Kompleksitas : `O(M logM)`
4. Menyimpan semua code tiap karakter byte pada hashmap dengan melakukan transversal dfs pada pohon huffman.<br>Kompleksitas `O(M)`
5. Melakukan output kode data dari hashmap (kompleksitas pengaksesan : `amortized O(1)`) tiap byte ke buffer.<br>Kompleksistas : `O(N)`

Kompleksistas total : `O(S + N + M logM)`

## Prosedur decompress

	Variable :
	N : Banyak data dalam bit

1. Membaca ekstensi file awal.
2. Membaca pohon huffman.
3. Membaca banyak bytes semula (dalam representasi integer).
4. Membaca data file semula sambil melakukan transversal pada pohon huffman.

Kompleksistas total : `O(N)`

# Tambahan

1. Kode penulisan data ke file diganti dari menggungakan `FileWriter` menjadi `FileOutputStream` untuk dapat menulis byte[] ke file, 
karena alasan representasi String dari byte[] tidak sama dengan data awal byte[] sehingga membuat error ketika program dekompresi dijalankan.
