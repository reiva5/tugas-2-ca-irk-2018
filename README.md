# Run Length Encoding (RLE) Data Compressor program
## Program Kompresi Data dengan Algoritma RLE

### Oleh Cornelius Yan M. - 13516113
#### dibuat untuk memenuhi Tugas 2 Ca-IRK 2018 : Pemampatan Data


Algoritma yang digunakan pada program pemampatan data ini adalah algoritma Run-length encoding (RLE).
RLE merupakan salah satu algoritma pemampatan data yang paling sederhana. Konsep utama algoritma RLE adalah menuliskan rangkaian data berurutan yang sama nilainya dengan jumlahnya kemunculannya, jika panjang rangkaian data tersebut lebih besar atau sama dengan 2. Algoritma ini akan terasa sangat berguna pada data yang mengandung banyak rangkaian data sama tersebut, namun justru tidak akan efektif jika data awal yang dimampatkan tidak memiliki rangkaian data yang sama tersebut (oleh karena itu, efektifitas program ini akan terasa jika data yang dimasukkan memiliki banyak rangkaian nilai nilai yang sama, seperti file teks yang memiliki banyak rangkaian atau gambar grafik sederhana seperti icon-icon, gambaran garis, dan animasi).
Berikut adalah contoh cara kerja pemampatan data dengan algoritma RLE :
  Data awal : ABBCCCDDDDEEEEEFFFFFFGGGGGGGHHHHHHHH (36 bytes)
  Data setelah dimampatkan : ABB2CC3DD4EE5FF6GG7HH8 (19 bytes saja)
  

Kompleksitas algoritma pada algoritma RLE adalah O(n) baik untuk melakukan kompresi maupun dekompresi.
Untuk fungsi doCompress(), program akan melakukan pembacaan seluruh isi file secara byte per byte sambil melakukan perhitungan dan langsung menuliskannya pada file baru yang berekstensi .irk, sehingga kompleksitasnya menjadi O(n) dengan n adalah total byte dari file awal.
Untuk fungsi decompress(), program akan melakukan pembacaan seluruh isi file .irk secara byte per byte kembali dan sambil menuliskannya pada file berekstensi awal pula, sehingga kompleksitasnya menjadi O(n) juga dengan n adalah total byte dari file Awal (bukan file .irk karena pada decompress terjadi pengulangan-pengulangan penulisan byte yang total pengulangannya sama dengan total byte dari file awal).


Alasan memilih algoritma RLE yaitu tentunya karena merupakan algoritma yang mudah dimengerti & diimplementasikan pada program pemampatan data sederhana. Selain itu, jika file yang ingin dimampatkan pun memiliki banyak rangkaian nilai sama, maka algoritma RLE ini dapat melakukan pemampatan yang sangat efisien. Terakhir, algoritma RLE juga memiliki kompleksitas yang cepat, hanya O(n) saja.

