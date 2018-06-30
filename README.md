# Tugas 2 Ca-IRK 2018

	Nama : Hagai Raja Sinulingga
	NIM  : 13516136
	
# Algoritma

Algoritma yang digunakan dalam program kompresi ini adalah algoritma Lempel–Ziv–Welch (LZW).
Algoritma LZW adalah algoritma kompresi yang lossless universal yang diciptakan oleh Abraham Lempel, Jacob Ziv, and Terry Welch. Dengan menggunakan teknik mengencode sebuah data dengan ukuran statis yang lebih besar, ide algoritma ini cocok dalam menyimpan data yang banyak redundan seperti pada ekstensi .GIF yang menyimpan pergerakan gambar kecil yang berubah sedikit demi sedikit saja. Algoritma LZW menggunakan struktur dictionary dalam pemrosesannya.

Saya memilih algoritma ini karena cukup efisien dan merupakan satu loncatan besar di dalam perjalanan riset kompresi data. Walaupun cukup sulit dalam mengimplementasikan algoritma ini, saya bersyukur bisa belajar dan mengeksplornya di masa libur saya. 

# Implementasi dan Analisis Kompleksistas

## Dictionary Pada File

### Representasi

1. Untuk tiap huruf ASCII dasar mengisi 255 kunci pertama
2. Susunan kemungkinan prefix disimpan mulai dari kunci 256, dst.

### Cara Baca Representasi

1. Kunci pertama dibaca sebagai ASCII (pasti akan demikian) sampai ada kunci diatas 255.
2. Jika ditemukan demikian, akan dibaca sebagai kombinasi prefix yang mungkin.

## Representasi Data Hasil Kompresi

Saya berhasil memampatkan data menjadi dalam bentuk key Long. Namun saya gagal mengubahnya dalam bentuk Byte sehingga untuk file yang tidak berupa teks tidak berhasil dikompresi (Long terbatas representasinya). Akhirnya saya menyimpan data dalam Long berbentuk string yang akhirnya terkadang justru juga membuat hasil kompresi lebih besar (terutama ketika dibandingkan dengan mengkompres berekstensi .PDF).

## Fungsi doCompress

	Variable :
	N : Banyak data dalam Long
	S : Panjang nama file
	M : banyak prefix berulang dalam file

1. Melakukan loop pada nama file untuk mendapatkan ekstensi.<br>Kompleksistas : `O(S)`
2. Mentraversal seluruh data dalam Long. <br>Worst Case Kompleksistas : `O(N)`. <br>Best Case Kompleksistas : `O(log(N))`. <br>Average Case Kompleksistas : `O((N+log(N))/2)`
3. Menghasilkan Kunci.<br>Kompleksistas : `O(1)`
4. Menyimpan semua key dari dictionary yang sudah di-generate ke variabel hasil.<br>Kompleksitas `O(M+255)`

Kompleksistas total : `O(S + ((N+log(N))/2) + 1 + M + 255)`

## Prosedur decompress

	Variable :
	N : Banyak data dalam long

1. Membaca ekstensi file awal. <br>Kompleksistas : `O(N/2)`
2. Mentraversal satu per satu data Long dan men-generate dictionary.<br>Kompleksistas : `O(N)`
3. Menyimpan ke dalam file.<br>Kompleksistas : `O(N)`

Kompleksistas total : `O(5N/2)`

# Tambahan

1. Kode penulisan data ke file ditambahkan exception untuk dapat menulis String ke file.
2. Program ini sangat baik apabila digunakan untuk mengcompress file text seperti pada 1.txt