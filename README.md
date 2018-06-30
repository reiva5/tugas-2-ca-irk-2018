# Ｌｏｓｓless Data Compression
***
## (c) Ihsan M. A. / 135-16-028
***

## Implementasi
***
Algoritma yang digunakan untuk memampatkan data adalah algoritma LZW. Sebuah lossless data compression algorithm yang dibuat oleh Abraham Lempel, Jacob Ziv, dan Terry Welch.

Algoritma ini cukup umum digunakan, dan mudah diimplementasikan. Karena algoritma ini mencari pengulangan pattern, space yang bisa dihemat berbanding lurus dengan banyaknya pattern yang berulang. 

Compression algorithm:
	
	1. Buat dictionary setiap substring dengan panjang 1

	2. Input string dari stream, dapatkan sebuah string W terpanjang yang ada di dalam dictionary

	3. Output nilai dari string W, keluarkan W dari stream

	4. Tambahkan W dengan karakter selanjutnya ke dalam dictionary

	5. Ulangi step 2

Untuk melakukan decoding, harus diketahui dictionary awal yang digunakan (pada kasus ini, ASCII tabel). Key tambahan dapat dibuat kembali.

Decompression algorithm:

	1. Baca bits input

	2. Jika bits ada di dictionary, maka decode bits

	3. Jika tidak, key baru bisa dibuat dengan melakukan concatenation dengan key sebelumnya

***

## Complexity
***
Compression:

- Time: 
	- O(N) dengan N adalah banyak karakter di dalam file (saat melakukan input). Sliding window membuat setiap karakter hanya dibaca sekali

	- O(1) untuk operasi pada dictionary (insertion/lookup)

	- O(N) secara keseluruhan

- Memory:
	- O(N) dengan N adalah banyak karakter di dalam file (char stream)

	- O(1) untuk dictionary (constant number of keys)

	- O(N) secara keseluruhan
	
Decompression:

- Time: 
	- O(N) dengan N adalah banyak bits yang disimpan di dalam file

	- O(1) untuk operasi pada dictionary (insertion/lookup)

	- O(N) secara keseluruhan

- Memory:
	- O(N) dengan N adalah banyak bits di dalam file (char stream)

	- O(1) untuk dictionary (constant number of keys)

	- O(N) secara keseluruhan
***

## Alasan
***
Alasan menggunakan algoritma LZW:

- Mudah

- Umum digunakan (Command "compress" pada UNIX dan file format _GIF_)

- Memahami dan belajar mengimplementasikan sliding window algorithm 