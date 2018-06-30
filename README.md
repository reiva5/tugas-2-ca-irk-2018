PEMBUAT
---------------------------------------------------------------------------------------------------
Nama	: Ferdiant Joshua Muis
NIM		: 13516047

Penjelasan
---------------------------------------------------------------------------------------------------
Teknik Run-Length Encoding adalah salah satu teknik kompresi data lossless yang sederhana dan sering digunakan.

Sebelum kompresi

    AAAAKKKUUUUUUU
  
Setelah kompresi

    AA4KK3UU7
    
Pada contoh di atas, jumlah karakter setelah dikompresi menjadi berkurang, dari awalnya 14 karakter menjadi 9 karakter.
Kompresi RLE menyimpan setiap karakter beserta jumlahnya jika berulang secara berurut. Jenis data yang dikompresi akan sangat mempengaruhi hasil kompresi.
Kompresi dengan metode RLE sederhana dan mudah untuk diimplementasikan, walaupun tidak selalu menghasilkan hasil file yang lebih kecil dibanding sebelum kompresi.


Kompleksitas
---------------------------------------------------------------------------------------------------
Kompleksitas algoritma ini sangat sederhana yaitu O(n), dengan n adalah jumlah byte dalam file yang akan dikompresi. Karena algoritma hanya beriterasi pada setiap byte yang terdapat pada file.


Alasan Pemilihan Algoritma
---------------------------------------------------------------------------------------------------
Saya memilih algoritma RLE karena metode ini sederhana baik dalam algoritma maupun pengimplementasiannya. Selain itu algoritma ini cukup efektif untuk mengompresi file-file berukuran kecil (<256 MB) yang cocok untuk pengerjaan tugas ini sebagai bentuk eksplorasi ilmu kompresi.
