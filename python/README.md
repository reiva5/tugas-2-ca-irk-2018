# Tugas-2-Ca-IRK
Repository Git untuk tugas 2 ca-IRK

Implementasi yang saya buat adalah algoritma huffman. Pembuatan algoritma huffman ini bertujuan karena mudah dipahami, dan merupakan aplikasi dari mata kuliah strategi algoritma tentang algoritma *greedy* dan mata kuliah matematika diskrit tentang pohon huffman.

m menyatakan rata-rata huruf per baris di teks input
n menyatakan jumlah baris pada teks input
o menyatakan jumlah karakter berbeda pada teks input
p menyatakan jumlah bit setelah *encoding*
q menyatakan jumlah bit yang dihilangkan menurut padded_info(maksimal 8 bit)

Kompleksitas algoritma huffman yang saya buat:
doCompress:
O(n) pada readlines(baris 125)
O(mno2) pada persiapan prosedur counting, prosedur counting, dan pada prosedur adder(baris 128-129, 58-73)
O(o log o) pada prosedur itemgetter(baris 130)
O(o2) pada prosedur copy(baris 131)
O(o2) + O(o) = O(o2) pada prosedur making path(yang O(o2)  untuk looping dan O(o) untuk reverse)(baris 133, 75-99)
O(o2) pada prosedur addNode di kelas Huffman Tree untuk pembuatan Huffman Tree (baris 134-136, 6-23)
O(o2) pada prosedur encode di kelas Huffman Tree untuk pembuatan Huffman Tree (baris 139, 24-32)
O(mno) pada prosedur counting, prosedur compress dan find_char(baris 141, 101-117)
O(o)+O(mn) pada penulisan ke file_huffman_path
Jadi, kompleksitas algoritma doCompress adalah O(mno2)

decompress:
O(p)+O(mn) = O(p) pada prosedur readinghuffman (baris 217, 174-202)
O(p) pada prosedur transformasi dari byte ke bit string(baris 220-225)
O(q) pada prosedur remove_padding(baris 226, 167-172)
O(p) pada prosedur decoding(baris 233, 204-209)
Jadi, kompleksitas algoritma decompress adalah O(p)

Namun, saya masih belum dapat menyertakan pembacaan file PDF sehingga saya akan menyertakan beberapa sampel teks, untuk penggunaannya sendiri, saya akan melampirkan file makefile yang sudah dimodifikasi agar membaca file teks saja.

Terima kasih kepada Bhirigu Srivastava atas idenya dan teks input yang bisa digunakan sebagai file input. Untuk selengkapnya, dapat dilihat di: [Bhirigu Srivastava's blog](http://bhrigu.me/blog/2017/01/17/huffman-coding-python-implementation/)

Nama: Kevin Andrian Liwinata
NIM: 13516118
