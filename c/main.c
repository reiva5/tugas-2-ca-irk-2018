#include <stdio.h>
#include <string.h>
#include <time.h>
#include "Compressed.h"

Compress tmp;
int main(int argc, char* argv[]){
	int query = argv[2][0] - '0';
	if (query == 1){
		clock_t begin = clock();
		tmp = doCompress(argv[1]);
		clock_t end = clock();
		char* fileName;
		for (int i = strlen(argv[1])-1; i >= 0; --i){
			if (argv[1][i] == '.'){
				fileName = (char*) malloc((i+3) * sizeof(char));
				int j;
				for (j = 0; j < i; ++j){
					fileName[j] = argv[1][j];
				}
				fileName[j++] = '.';
				fileName[j++] = 'i';
				fileName[j++] = 'r';
				fileName[j++] = 'k';
			}
		}
		FILE *f = fopen(fileName, "w");
		fprintf(f, "%s\n", toBytes(&tmp));
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