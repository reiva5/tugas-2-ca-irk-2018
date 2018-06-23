#ifndef COMPRESSED_H
#define COMPRESSED_H

typedef struct Compress {

} Compress;

unsigned char* toBytes(Compress*);
Compress doCompress(char* namaFile);
void decompress(char* namaFile);

#endif