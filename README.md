NAMA : JOSE HOSEA
NIM : 13516027
#### Penjelasan
Kompresi dan dekompresi dilakukan dengan menggunakan algoritma LZW, yaitu algoritma yang mencatat pola dalam file dan membuat kamus secara dinamis lalu mengubah pola-pola yang ditemukan menjadi indeks kamus. Hasil algoritma kompresi berupa sekuens angka yang merepresentasikan indeks dari kamus dinamis yang telah tersusun selama pembacaan file. Setelah itu sekuens angka di-pack ke dalam file dengan melakukan encode integer untuk mengurangi ukuran data. Untuk dekompresi, file dibaca lalu diproses dengan algoritma yang juga membentuk kamus dinamis yang sama dengan kamus sebelumnya(ketika file di-kompresi) dan melakukan pemetaan dari sekuens angka menjadi indeks kamus sehingga didapatlah entri kamus yang merupakan data sebenarnya.

#### Kompleksitas
(1)Algoritma kompresi LZW melakukan pembacaan data per byte dari file sebanyak 1 kali. (2)Lalu data diproses dengan melakukan pencarian data tersebut pada kamus. (3)Kemudian sekuens angka yang dihasilkan di-encode dengan pemrosesan setiap angka sebanyak 1 kali.
Dari ketiga proses di atas, proses kedualah yang paling lama untuk dieksekusi membutuhkan operasi sebanyak _n_ x _m_ kali, dengan _n_ adalah banyak input data dan _m_ adalah banyak entri kamus.

(1)Algoritma dekompresi LZW melakukan pembacaan data per byte dari file sebanyak 1 kali, lalu data di-decode untuk mendapatkan sekuens angka. (2)Lalu sekuens angka diproses dengan mengakses data berdasarkan indeks pada kamus. (3)Kemudian data yang berupa sekuens angka diartikan menjadi data yang sebenarnya dan dituliskan ke dalam file.
Ketiga proses tersebut melakukan pengulangan yang linier sehingga operasi yang dilakukan masing-masing proses adalah _n_ kali, dengan _n_ adalah banyak input data.

#### Alasan pemilihan algoritma
Algoritma LZW mudah dimengerti dan implementasi nya tidak terlalu sulit.

#### Tambahan
- Karena implementasi yang kurang optimal, hasil kompresi lebih sering berukuran lebih besar dari file asal.
- Karena perbedaan implementasi penulisan data ke file antara algoritma kompresi yang telah dibuat dengan algoritma pada _main.java_ , algoritma dekompresi yang telah dibuat tidak dapat membaca data pada file hasil kompresi dengan baik, dan menghasilkan error. Karena itu terdapat program yang disertakan sebagai alternatif untuk melakukan dekompresi file yaitu _main_old.java_.
