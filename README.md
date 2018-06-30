# Data Compression 
Tugas 2 Ca-IRK 2018

## Pembuat
Kurniandha Sukma Yunastrian - 13516106

## Implementasi
### Penjelasan
Implementasi dari pemampatan data yang dibuat adalah dengan menggunakan algoritma **_Run-Length Encoding_**. Algoritma ini menjalankan data (yaitu, urutan di mana nilai data yang sama terjadi di banyak elemen data yang berurutan) disimpan sebagai satu nilai data dan hitungan, bukan dibandingkan dengan yang asli. <br>

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
_Method_ **_deCompress_** memiliki kompleksitas yang sama dengan _Method_ **_doCompress_** yaitu **O(n)** dimana **n** adalah panjang data. Hal ini disebabkan karena data string _bytes_ akan dicek per karakter secara berurutan hingga karakter terakhir.

## Tambahan
Algoritma **_RLE_** yang saya buat tidak sepenuhnya seperti penjelasan di atas. Terdapat beberapa hal yang dilakukan yaitu : <br>
### \#1
Jika jumlah karakter yang sedang diperiksa adalah **< 4**, maka ditulis apa adanya. <br>
contoh : <br>
  Terdapat string sebagai berikut 
```
AABCCC
```
  Hasil kompresinya adalah 
```
AABCCC
```
  bukan **A2B1C3**

### \#2
Jika jumlah karakter yang sedang diperiksa adalah **>= 4**, maka ditulis menggunakan konsep **_RLE_**. Algoritma yang saya implementasi tidak sepenuhnya mirip dengan konsep, tetapi terdapat modifikasi sehingga dapat dibedakan antara angka penanda kompresi dengan angka byte sebenarnya. Penanda tersebut diikuti string "\`\[". Dipilih string tersebut karena kemunculan kedua string tersebut secara berurutan tidak saya temukan selama mengerjakan tugas ini. <br>
contoh : <br>
  Terdapat string sebagai berikut
```
AABBBBBBB45LLLL1
```
  Hasil kompresinya adalah
```
AAB`[745L`[41
```

### \#3
Terdapat spesifikasi yaitu program dapat mengembalikan file yang telah dikompresi menjadi file semula. Maka dari itu, ekstensi dari file semula juga disimpan di dalam file hasil kompresi yang terletak di awal file. Format penulisan di file hasil kompresi adalah **\<panjang karakter ekstensi\>\<ekstensi\>\<byte file\>**. <br>
  Contoh : Misalkan terdapat file dengan nama **"ini_file.haha"**. Maka, hasil penyimpanan di file kompresi adalah **"4haha\<byte file\>"**.
