import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

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
		// File file = new File(nameFile);
		// byte[] filedata = new byte[(int) file.length()];
		// DataInputStream dis = new DataInputStream(new FileInputStream(file));
		// dis readFully(filedata);
		try{
			byte[] bytes = Files.readAllBytes(Paths.get(nameFile));
			Byte[] Bytes = new Byte[bytes.length];
			for(int i=0;i<bytes.length;++i)
				Bytes[i] = bytes[i];
			// LZ77.compress(bytes);
			Huffman.<Byte>compress(Bytes);
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

class LZ77{
	private final static int dictlength = 8191;
	private final static int lookuplength = 1023;
	public static void compress(byte[] data){
		ArrayList<Pair<Pair<Integer, Integer>, Byte>> output = new ArrayList<Pair<Pair<Integer, Integer>, Byte>>();
		int n = data.length-1;
		int l=0, r = n, dl=0;
		if(n > lookuplength)
			r = lookuplength;
		while(l < n){
			Pair<Integer, Integer> tmp = findmatch(data, dl, l, r);
			if(l+tmp.getSecond() >= r)
				tmp.setSecond(r-l-1);
			output.add(new Pair<Pair<Integer, Integer>, Byte> (tmp, data[l+tmp.getSecond()]));
			l += tmp.getSecond()+1;
			r += tmp.getSecond()+1;
			if(r > n)
				r = n;
			if(l - dl > dictlength){
				dl = l - dictlength;
			}
		}
		System.out.println("data size "+data.length+" become "+(4*output.size()));
	}
	private static Pair<Integer, Integer> findmatch(byte[] data, int dl, int l, int r){
		int len=0, idx=0;
		int[] ft = null;
		if(dl < l)
			ft = preprocessKMP(data, l, r);
		for(int i=dl, j=l;i<l;){
			if(data[i] == data[j]){
				++j; ++i;
				if((j == r || i == l) && j-l > len){
					len = j-l;
					idx = i;
					break;
				}
			}
			else if(j>l){
				if(j-l > len){
					len = j-l;
					idx = i;
				}
				j = ft[j-l];
			}
			else
				i++;
		}
		return new Pair<Integer, Integer>(l-idx+len, len);
	}
	private static int[] preprocessKMP(byte[] data, int dl, int dr){
		int length = dr-dl+1;
		int[] ft = new int[length];
		ft[0] = dl-1;
		ft[1] = dl;
		for(int l=dl,r=dl+2;r<dr;){
			if(data[r-1] == data[l]){
				ft[r-dl] = ++l;
				r++;
			}
			else if(l>dl)
				l = ft[l-dl];
			else{
				ft[r-dl] = l;
				r++;
			}
		}
		return ft;
	}
	private static void printdata(byte[] data, int start, int len){
		for(int i=0;i<len;++i){
			System.out.print("<"+data[start+i]+">");
		}
	}
	private static ArrayList<Byte> decompress(ArrayList<Pair<Pair<Integer, Integer>, Byte>> data){
		return null;
	}
}

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
	public static <T extends Number> void compress(T[] data){
		Map<T, Integer> frek = new HashMap<T, Integer>(); 
		int n = data.length;
		for(T tmp : data){
			Integer counter = frek.get(tmp);
			if(counter == null)
				frek.put(tmp, 1);
			else
				frek.put(tmp, counter+1);
		}
		PriorityQueue<Pair<Integer, Tree<T>>> pq = new PriorityQueue<Pair<Integer, Tree<T>>>(frek.size(), new  FrequentComparator<T>());
		for(Map.Entry<T, Integer> entry : frek.entrySet())
			pq.add(new Pair<Integer, Tree<T>>(entry.getValue(), new Tree<T>(entry.getKey())));
		while(pq.size()>1){
			Pair<Integer, Tree<T>> left = pq.poll();
			Pair<Integer, Tree<T>> right = pq.poll();
			pq.add(new Pair<Integer, Tree<T>>(left.getFirst()+right.getFirst(), new Tree<T>(left.getSecond(), right.getSecond())));
		}
		Tree<T> root = pq.poll().getSecond();
		HashMap<T, String> findcode = new HashMap<T, String>();
		getCode(findcode, root, "");
		int datalength = 0;
		for(T tmp : data){
			String code = findcode.get(tmp);
			datalength += code.length();
			// for(int i=0;i<code.length();++i){
				// if(code.charAt(i)=='0'){
					
				// }
				// else{
					
				// }
			// }
		}
		System.out.println("data size "+data.length+" become "+(datalength/8));
	}
	private static<T> void getCode(HashMap<T, String> code, Tree<T> node, String str){
		if(node.getValue()==null){
			getCode(code, node.getLeftChild(), str+"0");
			getCode(code, node.getRightChild(), str+"1");
		}
		else{
			code.put(node.getValue(), str);
		}
	}
	private static class FrequentComparator<T> implements Comparator<Pair<Integer, Tree<T>>>{
		@Override
		public int compare(Pair<Integer, Tree<T>> a, Pair<Integer, Tree<T>> b){
			int x = a.getFirst() - b.getFirst();
			if(x == 0)
				return 0;
			else
				return x<0? -1:1;
		}
	}
	private static class Tree<T>{
		private T value;
		private Tree<T> left, right, parent;
		public Tree(){
			value = null;
			left = right = parent = null;
		}
		public Tree(T val){
			value = val;
			left = right = parent = null;
		}
		public Tree(Tree<T> le, Tree<T> ri){
			value = null;
			parent = null;
			left = le;
			right = ri;
		}
		public void setLeftChild(Tree<T> le){left = le;}
		public void setRightChild(Tree<T> ri){right = ri;}
		public void setParent(Tree<T> par){parent = par;}
		public void setValue(T val){value = val;}
		public Tree<T> getLeftChild(){return left;}
		public Tree<T> getRightChild(){return right;}
		public Tree<T> getParent(){return parent;}
		public T getValue(){return value;}
	}
}