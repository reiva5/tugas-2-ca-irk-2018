import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class Compressed {
	private ArrayList<Byte> data;
	private String ext;
	public Compressed(){
		data = null;
		ext = "";
	}
	public String toBytes(){
		String s = null;
		byte[] tmp = new byte[data.size()];
		for(int i=0;i<data.size();++i)
			tmp[i] = data.get(i);
		s = ext + (new String(tmp));
		// System.out.println(s.length());
		return s;
	}
	public Compressed doCompress(String nameFile){
		try{
			getExt(nameFile);
			byte[] databyte = Files.readAllBytes(Paths.get(nameFile));
			Map<Byte, Integer> frek = new HashMap<Byte, Integer>(); 
			int n = databyte.length;
			for(byte tmp : databyte){
				Integer counter = frek.get(tmp);
				if(counter == null)
					frek.put(tmp, 1);
				else
					frek.put(tmp, counter+1);
			}
			PriorityQueue<Pair<Integer, Tree>> pq = new PriorityQueue<Pair<Integer, Tree>>(frek.size(), new  FrequentComparator());
			for(Map.Entry<Byte, Integer> entry : frek.entrySet())
				pq.add(new Pair<Integer, Tree>(entry.getValue(), new Tree(entry.getKey())));
			if(pq.size()==1){
				Byte tmp = pq.peek().getSecond().getValue();
				pq.add(new Pair<Integer, Tree>(0, new Tree((byte) (tmp+1))));
			}
			while(pq.size()>1){
				Pair<Integer, Tree> left = pq.poll();
				Pair<Integer, Tree> right = pq.poll();
				pq.add(new Pair<Integer, Tree>(left.getFirst()+right.getFirst(), new Tree(left.getSecond(), right.getSecond())));
			}
			Tree root = pq.poll().getSecond();
			HashMap<Byte, String> findcode = new HashMap<Byte, String>();
			getCode(findcode, root, "");
			OutputArrayData out = new OutputArrayData();
			printTree(root, out);
			out.writeInt(databyte.length);
			for(Byte tmp : databyte){
				final String code = findcode.get(tmp);
				for(int i=0;i<code.length();++i){
					if(code.charAt(i)=='0')
						out.writeBit(false);
					else
						out.writeBit(true);
				}
			}
			data = out.getData();
			// System.out.println(databyte.length+" become "+data.size());
		}
		catch(IOException e){
			System.out.println(e);
		}
		return this;
	}
	public void decompress(String nameFile){
		try{
			InputArrayByte in = new InputArrayByte(nameFile);
			data = new ArrayList<Byte>();
			// read ext
			ext = "";
			char c;
			while( (c = (char) in.readByte()) != '/')
				ext += c;
			// read Tree
			Tree root = null;
			readTree(root, in);
			// read original data byte lenth
			int datasize = in.readInt();
			// read data
			for(int i=0;i<datasize;++i)
				data.add(readCodeData(root, in));
			// Write to file :)
			// closing file :)
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return;
	}
	/* YOU CAN ADD ANOTHER METHOD OR VARIABLE BELOW HERE */
	private void readTree(Tree node, InputArrayByte in) throws IOException{
		if(in.readBit())
			node = new Tree(in.readByte());
		else{
			Tree right=null, left=null;
			readTree(left, in);
			readTree(right, in);
			node = new Tree(left, right);
		}
	}
	private Byte readCodeData(Tree node, InputArrayByte in) throws IOException{
		if(node.getValue()==null){
			if(in.readBit())
				return readCodeData(node.getRightChild(), in);
			else
				return readCodeData(node.getLeftChild(), in);
		}
		else
			return node.getValue();
	}
	private void getExt(String nameFile){
		int lastdot = -1;
		for(int i=0;i<nameFile.length();++i){
			if(nameFile.charAt(i)=='.')
				lastdot = i;
		}
		ext = "";
		for(int i=lastdot+1;i<nameFile.length();++i)
			ext += nameFile.charAt(i);
		ext += "/";
	}
	private void getCode(HashMap<Byte, String> code, Tree node, String str){
		if(node.getValue()==null){
			getCode(code, node.getLeftChild(), str+"0");
			getCode(code, node.getRightChild(), str+"1");
		}
		else{
			code.put(node.getValue(), str);
		}
	}
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
	public ArrayList<Byte> getData(){
		if(counter<3)
			flush();
		return data;
	}
}

class InputArrayByte{
	private static final int maxsize = 1000000000;
	private DataInputStream in;
	private byte[] data;
	private int idx, bit, len;
	public InputArrayByte(String filename) throws IOException{
		in = new DataInputStream(new FileInputStream(filename));
		data = new byte[maxsize];
		idx = bit = 0;
		readData();
	}
	public void readData() throws IOException{
		len = in.read(data);
		if(len==-1)
			data[0] = 0;
	}
	public boolean readBit() throws IOException{
		byte val = (byte)(1<<bit);
		val &= data[idx];
		if(++bit == 8){
			bit=0;
			if(++idx == len)
				readData();
		}
		return val > 0;
	}
	public byte readByte() throws IOException{
		byte val = 0;
		for(int i=0;i<8;++i){
			val <<= 1;
			if(readBit())
				val +=1;
		}
		return val;
	}
	public int readInt() throws IOException{
		int val = 0;
		for(int i=0;i<32;++i){
			val <<= 1;
			if(readBit())
				val +=1;
		}
		return val;
	}
}