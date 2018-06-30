import os
import itertools


class Compressed:
    filename = ""
    extension = ""
    data = bytearray()

    def __init__(self):
        print("done")

    def toBytes(self):
        return self.data

    def set_filename(self, filename):
        self.filename, self.extension = os.path.splitext(filename)
        self.data=str.encode(self.extension[1:]) + (b'\00')

    def compress_data(self, f):
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
                    self.data+=(byte)
                else:
                    if byte != prev or count == 255:

                        self.data+=(bytes([count]))
                        count = 1
                        prev = byte
                        self.data+=(byte)
                    else:
                        count += 1
                byte = f.read(1)
        finally:
            f.close()
            if count >= 2:
                self.data+=(bytes([count]))

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

        self.decompress_data(fr, fw)

        fr.close()
        fw.close()
        print("Decompressed File : "+decompressedFileName)

    def decompress_data(self, f, fw):
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
                    count = int.from_bytes(byte, byteorder='little') - 2
                    for _ in itertools.repeat(None, count):
                        fw.write(prev)
                    prev = b''
                    runs = False
                    count = 0
                byte = f.read(1)
        finally:
            f.close()
            if runs:
                count = int.from_bytes(byte, byteorder='little') - 2
                for _ in itertools.repeat(None, count):
                    fw.write(prev)


def doCompress(namaFile):
    compress = Compressed()
    compress.set_filename(namaFile)

    fr = open(compress.filename+compress.extension, "rb")

    compress.compress_data(fr)

    fr.close()
    return compress


def decompress(namaFile):
    compress = Compressed()
    compress.set_filename(namaFile)
    compress.toOrigin()
    return
