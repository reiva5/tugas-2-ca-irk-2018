#ifndef COMPRESSED_H
#define COMPRESSED_H

class Compress {
	public:
		unsigned char* toBytes();
	/* YOU CAN ADD ANOTHER METHOD OR VARIABLE BELOW HERE */
};

Compress doCompress(char* namaFile);
void decompress(char* namaFile);

#endif