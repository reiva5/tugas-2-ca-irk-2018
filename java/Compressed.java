import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Arrays;

public class Compressed {
	private String nameFile = "Asu";
	public Compressed(){
		nameFile = "susu";
	}
	
	public String toBytes(){
        
        String bForm = "";

        File file = new File(nameFile);

        byte[] b = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(b);
        for (int i = 0; i < b.length; i++) {
            bForm += (char)b[i];
        }
		return bForm;
	}

	public Compressed doCompress(String nameFile){
		File f1 = new File(nameFile);
		FileInputStream fis1 = new FileInputStream(nameFile);
		FileInputStream fis2 = new FileInputStream(nameFile);
		FileOutputStream fos = new FileOutputStream(nameFile);
		Compressed compressed = new Compressed();
		compressed.compressHuffman(fis1, fis2, fos);

		return compressed;
	}
	public void decompress(String nameFile){
		return;
	}

	public String compressHuffman(InputStream in1, InputStream in2, OutputStream out){
		WriteBuffer wb = new WriteBuffer(out, true);
		byte[] buffer = new byte[10240];
			
		//Get frequency of each character in the input
		int searching = in1.read(buffer);
		int[] frequency = new int[256];
		while (searching > 0) {
			for (int i = 0; i < searching; i++) {
				frequency[(int) buffer[i] + 128]++;
			}
			searching = in1.read(buffer);
		}
		in1.close();
		//Put the frequencies into a priority queue as Huffman trees
		PriorityQueue<HuffmanTree> ph = new PriorityQueue();
		HuffmanTree null_tree = new HuffmanTree();
		for (int i = 0; i < 256; i++) {
			if (frequency[i] > 0) {
				ph.offer(new HuffmanTree((byte) (i - 128), null_tree,
				null_tree, frequency[i]));
			}
		}
		//This stops a bug where there is only one sort of byte in a file
		if (ph.size() == 1) {
			ph.offer(new HuffmanTree((byte) 0, null_tree, null_tree, 0));
		}
		//Build a Huffman Tree
		while (ph.size() > 1) {
			HuffmanTree bt_get1 = ph.poll();
			HuffmanTree bt_get2 = ph.poll();
			HuffmanTree add = new HuffmanTree(bt_get1, bt_get2,
			bt_get1.frequency + bt_get2.frequency);
			ph.offer(add);
		}
		//Get our huffman tree
		HuffmanTree htree = ph.poll();
		//Write our huffman tree to the output
		wb.write(htree.toBooleanArray());
		//Grab a boolean[][] so we can convert the second stream
		boolean[][] huffmantreeArrayList = htree.toArrayList();
			
		//Convert data to compressed stream
		searching = in2.read(buffer);
		while (searching > 0) {
			for (int i = 0; i < searching; i++) {
				byte characterByte = buffer[i];
				boolean[] characterBoolArray =
					huffmantreeArrayList[(int) characterByte + 128];
				wb.write(characterBoolArray);
			}
			searching = in2.read(buffer);
		}
		//Flush out output so everything is written
		wb.flush();
		in2.close();
		out.close();
		
		return null;
	}
}


