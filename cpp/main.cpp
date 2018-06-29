#include <bits/stdc++.h>
#include "Compressed.h"

using namespace std;

Compress tmp;
int main(int argc, char* argv[]){
	int query = argv[2][0] - '0';
	if (query == 1){
		clock_t begin = clock();
		tmp = doCompress(argv[1]);
		clock_t end = clock();
		string fileName = "";
		for (int i = strlen(argv[1])-1; i >= 0; --i){
			if (argv[1][i] == '.'){
				for (int j = 0; j < i; ++j){
					fileName += argv[1][j];
				}
			}
		}
		fileName += ".irk";
		ofstream out(fileName);
		out << tmp.toBytes() << endl;
		out.close();
		printf("Ukuran hasil kompresi adalah: %ld byte(s)\n", sizeof(tmp));
		printf("Eksekusi waktu hasil kompresi adalah: %.03f second(s)\n", (double) (end - begin)/CLOCKS_PER_SEC);
	} else {
		clock_t begin = clock();
		decompress(argv[1]);
		clock_t end = clock();
		printf("Eksekusi waktu hasil de-kompresi adalah: %.03f second(s)\n", (double) (end - begin)/CLOCKS_PER_SEC);
	}
	return 0;
}