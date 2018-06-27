import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class Compressed {
	private String asu = "Asu";
	public Compressed(){
		asu = "susu";
	}
	public String toBytes(){
		return "";
	}
	public Compressed doCompress(String nameFile){
		Compressed compressed = new Compressed();
		try{
			byte[] bytes = Files.readAllBytes(Paths.get(nameFile));
			// LZ77.compress(bytes);
			ArrayList<Byte> result =  Huffman.compress(bytes);
			System.out.println(bytes.length+" become "+result.size());
		}
		catch(IOException e){
			System.out.println(e);
		}
		return compressed;
	}
	public void decompress(String nameFile){
		
		return;
	}
	/* YOU CAN ADD ANOTHER METHOD OR VARIABLE BELOW HERE */
};

class Pair <T, U>{
	private T fi;
	private U se;
	public Pair(T first, U second){
		fi = first;
		se = second;
	}
	public void setFirst(T first){ fi = first; }
	public void setSecond(U second){ se = second; }
	public T getFirst(){ return fi; }
	public U getSecond(){ return se; }
}

class Huffman{
	public static ArrayList<Byte> compress(byte[] data){
		Map<Byte, Integer> frek = new HashMap<Byte, Integer>(); 
		int n = data.length;
		for(byte tmp : data){
			Integer counter = frek.get(tmp);
			if(counter == null)
				frek.put(tmp, 1);
			else
				frek.put(tmp, counter+1);
		}
		PriorityQueue<Pair<Integer, Tree>> pq = new PriorityQueue<Pair<Integer, Tree>>(frek.size(), new  FrequentComparator());
		for(Map.Entry<Byte, Integer> entry : frek.entrySet())
			pq.add(new Pair<Integer, Tree>(entry.getValue(), new Tree(entry.getKey())));
		while(pq.size()>1){
			Pair<Integer, Tree> left = pq.poll();
			Pair<Integer, Tree> right = pq.poll();
			pq.add(new Pair<Integer, Tree>(left.getFirst()+right.getFirst(), new Tree(left.getSecond(), right.getSecond())));
		}
		Tree root = pq.poll().getSecond();
		HashMap<Byte, String> findcode = new HashMap<Byte, String>();
		getCode(findcode, root, "");
		OutputArrayData out = new OutputArrayData();
		int datalength = 0;
		for(Map.Entry<Byte, Integer> entry : frek.entrySet())
			datalength += entry.getValue()*(findcode.get(entry.getKey()).length());
		printTree(root, out);
		out.writeInt(datalength);
		for(Byte tmp : data){
			String code = findcode.get(tmp);
			for(int i=0;i<code.length();++i){
				if(code.charAt(i)=='0')
					out.writeBit(false);
				else
					out.writeBit(true);
			}
		}
		System.out.println("data size "+data.length+" become "+(datalength/8));
		out.flush();
		return out.getData();
	}
	public static ArrayList<Byte> decompress(byte
	private static void printTree(Tree node, OutputArrayData out){
		if(node.getValue()==null){
			out.writeBit(false);
			printTree(node.getLeftChild(), out);
			printTree(node.getRightChild(), out);
		}
		else{
			out.writeBit(true);
			out.writeByte(node.getValue());
		}
	}
	private static void getCode(HashMap<Byte, String> code, Tree node, String str){
		if(node.getValue()==null){
			getCode(code, node.getLeftChild(), str+"0");
			getCode(code, node.getRightChild(), str+"1");
		}
		else{
			code.put(node.getValue(), str);
		}
	}
	private static class FrequentComparator implements Comparator<Pair<Integer, Tree>>{
		@Override
		public int compare(Pair<Integer, Tree> a, Pair<Integer, Tree> b){
			int x = a.getFirst() - b.getFirst();
			if(x == 0)
				return 0;
			else
				return x<0? -1:1;
		}
	}
	private static class Tree{
		private Byte value;
		private Tree left, right, parent;
		public Tree(){
			value = null;
			left = right = parent = null;
		}
		public Tree(Byte val){
			value = val;
			left = right = parent = null;
		}
		public Tree(Tree le, Tree ri){
			value = null;
			parent = null;
			left = le;
			right = ri;
		}
		public void setLeftChild(Tree le){left = le;}
		public void setRightChild(Tree ri){right = ri;}
		public void setParent(Tree par){parent = par;}
		public void setValue(Byte val){value = val;}
		public Tree getLeftChild(){return left;}
		public Tree getRightChild(){return right;}
		public Tree getParent(){return parent;}
		public Byte getValue(){return value;}
	}
}

class OutputArrayData{
	byte buffer, counter;
	ArrayList<Byte> data;
	public OutputArrayData(){
		counter = 7;
		buffer = 0;
		data = new ArrayList<Byte>();
	}
	public OutputArrayData(ArrayList<Byte> bytes){
		counter = 7;
		buffer = 0;
		data = bytes;
	}
	public OutputArrayData(Byte[] bytes){
		counter = 7;
		buffer = 0;
		for(Byte tmp : bytes)
			data.add(tmp);
	}
	public void writeBit(boolean data){
		if(data)
			buffer += 1<<counter;
		counter --;
		if(counter == -1)
			flush();
	}
	public void writeByte(byte data){
		data = reverse(data);
		for(int i=0;i<8;++i){
			buffer += (data&1) <<counter;
			data >>>=1;
			if(--counter == -1)
				flush();
		}
	}
	public void writeInt(int data){
		data = reverse(data);
		for(int i=0;i<32;++i){
			buffer += (data&1) <<counter;
			data >>>=1;
			if(--counter == -1)
				flush();
		}
	}
	public void flush(){
		data.add(buffer);
		buffer = 0;
		counter = 7;
	}
	private byte reverse(byte val){
		byte res = 0;
		for(int i=0;i<8;++i){
			res <<= 1;
			res |= val&1;
			val >>>=1;
		}
		return res;
	}
	private int reverse(int val){
		int res = 0;
		for(int i=0;i<4;++i){
			res <<= 1;
			res |= val&1;
			val >>>=1;
		}
		return res;
	}
	public ArrayList<Byte> getData(){return data;}
}