import os
import itertools


class Compress:
    filename = ""
    extension = ""

    def __init__(self, filename):
        self.filename, self.extension = os.path.splitext(filename)

    def toBytes(self):
        fr = open(self.filename+self.extension, "rb")
        compressedFileName = self.filename+".irk"
        fw = open(compressedFileName, "wb+")

        fw.write(str.encode(self.extension[1:]))
        fw.write(b'\x00')
        self.compress_data(fr,fw)

        fr.close()
        fw.close()
        print("Compressed File : "+compressedFileName)

    def compress_data(self, f,fw):
        prev = b''
        count = 0
        try:
            byte = f.read(1)
            while byte:
                if count < 2:
                    if byte != prev:
                        count = 1
                        prev = byte
                    else:
                        count = 2
                    fw.write(byte)
                else:
                    if byte != prev or count == 255:
                        
                        fw.write(bytes([count]))
                        count = 1
                        prev = byte
                        fw.write(byte)
                    else:
                        count += 1
                byte = f.read(1)
        finally:
            f.close()
            if count >= 2:
                fw.write(bytes([count]))

    def toOrigin(self):
        if self.extension != ".irk":
            print("Invalid file")
            return

        fr = open(self.filename+self.extension, "rb")

        extension = b''
        byte = fr.read(1)
        while byte != b'\x00':
            extension += byte
            byte = fr.read(1)
        self.extension = extension.decode()

        decompressedFileName = self.filename+"."+self.extension
        fw = open(decompressedFileName, "wb+")

        self.decompress_data(fr,fw)

        fr.close()
        fw.close()
        print("Decompressed File : "+decompressedFileName)

    def decompress_data(self, f,fw):
        prev = b''
        runs = False
        count = 0
        try:
            byte = f.read(1)
            while byte:
                if not runs:
                    if byte != prev:
                        prev = byte
                    else:
                        runs = True
                    fw.write(byte)
                else:
                    count = int.from_bytes(byte, byteorder='little') -2
                    for _ in itertools.repeat(None, count):
                        fw.write(prev)
                    prev = b''
                    runs = False
                    count = 0
                byte = f.read(1)
        finally:
            f.close()
            if runs:
                count = int.from_bytes(byte, byteorder='little') -2
                for _ in itertools.repeat(None, count):
                    fw.write(prev)


def doCompress(namaFile):
    compress = Compress(namaFile)
    compress.toBytes()
    return


def decompress(namaFile):
    compress = Compress(namaFile)
    compress.toOrigin()
    return
