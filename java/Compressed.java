import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Arrays;

public class Compressed {
	private byte[] b;
	private String nameFile;

	public Compressed(String nameFile){
		this.nameFile = nameFile;
		b = new byte[10240];	
	}

	public String toBytes(){
        
        String bForm = "";

        File file = new File(this.nameFile);

		try{
        	FileInputStream fileInputStream = new FileInputStream(file);
        	fileInputStream.read(b);
        	for (int i = 0; i < b.length; i++) {
            	bForm += (char)b[i];
			}
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return bForm;
	}

	public Compressed doCompress(String nameFile){
		File f1 = new File(nameFile);
		Compressed compressed = new Compressed(nameFile);

		try{
			FileInputStream fis1 = new FileInputStream(nameFile);
			FileInputStream fis2 = new FileInputStream(nameFile);
			FileOutputStream fos = new FileOutputStream(nameFile);
			compressed.compressHuffman(fis1, fis2, fos);
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
		return compressed;
	}

	public void decompress(String nameFile){
		File f1 = new File(nameFile);

		try{
			FileInputStream fis = new FileInputStream(nameFile);
			FileOutputStream fos = new FileOutputStream(nameFile);
			Compressed compressed = new Compressed(nameFile);
			compressed.decompressHuffman(fis, fos);

			fis.close();
			fos.close();
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

	public String compressHuffman(InputStream in1, InputStream in2, OutputStream out){
		try{
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
		}catch(IOException e){
			System.out.println(e.getMessage());
		}

		return null;
	}

	public String decompressHuffman(FileInputStream in, FileOutputStream out){
		try {
		WriteBuffer wb = new WriteBuffer(out, false);
		byte[] buffer = new byte[12288];
		boolean[] bufferbool = new boolean[8];
		boolean[] treeleaf = new boolean[8];
		boolean treefound = false;
		int numLeavesThisLevel = 0;
		int numLeaves = 0;
		int nodesFoundThisLevel = 0;
		int nodesThisLevel = 1;
		boolean treemade = false;
		int numLeavesFound = 0;
		int treeleafposition = 0;
			
		ArrayList<HuffmanTree> htreebuilder = new ArrayList();
		ArrayList<HuffmanTree> leaves = new ArrayList();
		htreebuilder.add(new HuffmanTree());
		HuffmanTree htree = htreebuilder.get(0);
		HuffmanTree currentTree = htree;
		int position = 0;
			
		int searching = in.read(buffer);
		while (searching > 0) {
			for (int i = 0; i < (searching - 2); i++) {
				bufferbool = BitByteConverter.byteToBooleanArray(buffer[i]);
				for (int j = 0; j < 8; j++) {
					if (!treefound) {
					//Make the tree
						if (bufferbool[j]) {
							//We have a leaf
							HuffmanTree lookingat = htreebuilder.get(position);
							leaves.add(lookingat);
								
							numLeavesThisLevel++;
							numLeaves++;
						}
						else {
							//No Leaf
							HuffmanTree lookingat = htreebuilder.get(position);
							lookingat.left = new HuffmanTree();
							lookingat.right = new HuffmanTree();
							htreebuilder.add(lookingat.getLeft());
							htreebuilder.add(lookingat.getRight());
								
							nodesFoundThisLevel++;
						}
						position++;
						if ((nodesFoundThisLevel + numLeavesThisLevel) == nodesThisLevel) {
							nodesThisLevel = nodesFoundThisLevel * 2;
							nodesFoundThisLevel = 0;
							numLeavesThisLevel = 0;
							if (nodesThisLevel == 0) {
								treefound = true;
							}
						}
					}
					else if (!treemade) {
						//Map characters to the tree
						treeleaf[treeleafposition] = bufferbool[j];
						treeleafposition++;
						if (treeleafposition == 8) {
							//We have the tree node
							HuffmanTree leaf = leaves.get(numLeavesFound);
							leaf.character = BitByteConverter.booleanArrayToByte(treeleaf);
							leaf.leafnode = true;
							treeleafposition = 0;
							numLeavesFound++;
								
							if (numLeavesFound == numLeaves) {
								treemade = true;
							}
						}
					}
					else {
						//Decompress the file
						if (!bufferbool[j]) {
							currentTree = currentTree.getLeft();
						}
						else {
							currentTree = currentTree.getRight();
						}
						if (currentTree.isLeaf()) {
							wb.write(currentTree.getCharacter());
							currentTree = htree;
						}
					}
				}
			}
			buffer[0] = buffer[searching - 2];
			buffer[1] = buffer[searching - 1];
			searching = in.read(buffer, 2, buffer.length - 2);
			if (searching != -1) {
				searching += 2;
			}
		}
		//System.out.println("Breaking Free!");
		//We get to here with only 2 bytes left
		//one of which tells us how much padding there is
		boolean[] paddingBits = BitByteConverter.byteToBooleanArray(buffer[0]);
		boolean[] lastBits = BitByteConverter.byteToBooleanArray(buffer[1]);
		int bytesToIgnore = 0;
		for (int i = 0; i < 8; i++) {
			if (lastBits[i]) {
				bytesToIgnore++;
			}
		}
		bufferbool = paddingBits;
		for (int j = 0; j < (8 - bytesToIgnore); j++) {
			if (!bufferbool[j]) {
				currentTree = currentTree.getLeft();
			}
			else {
				currentTree = currentTree.getRight();
			}
			if (currentTree.isLeaf()) {
				wb.write(currentTree.getCharacter());
				currentTree = htree;
			}
		}
		//Flush and close everything
		wb.flush();
		in.close();
		out.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return null;
	}
}


