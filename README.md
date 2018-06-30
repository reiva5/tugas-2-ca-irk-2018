# Data Compression 
Tugas 2 Ca-IRK 2018

## Pembuat
Kurniandha Sukma Yunastrian - 13516106

## Implementasi
### Penjelasan
Implementasi dari pemampatan data yang dibuat adalah dengan menggunakan algoritma **_Run-length encoding_**. Algoritma ini menjalankan data (yaitu, urutan di mana nilai data yang sama terjadi di banyak elemen data yang berurutan) disimpan sebagai satu nilai data dan hitungan, bukan dibandingkan dengan yang asli. <br>

### Alasan Pemilihan Algoritma
Digunakan algoritma ini karena sangat mudah dimengerti dan diimplementasikan.

### Contoh
Terdapat string sebagai berikut :
```
AAAAABBCCCCCCCDD
```
Dengan menggunakan algoritma **_RLE_**, didapatkan hasil sebagai berikut :
```
A5B2C7D2
```

## Analisis Kompleksitas
### doCompress
_Method_ **_doCompress_** memiliki kompleksitas **O(n)** dimana **n** adalah panjang data. Hal ini disebabkan karena data string _bytes_ akan dicek per karakter secara berurutan hingga karakter terakhir.

### deCompress
_Method_ **_deCompress_** memiliki kompleksitas yang sama dengan _Method_ **_deCompress_** yaitu **O(n)** dimana **n** adalah panjang data. Hal ini disebabkan karena data string _bytes_ akan dicek per karakter secara berurutan hingga karakter terakhir.
