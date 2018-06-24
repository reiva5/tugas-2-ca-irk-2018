import java.util.ArrayList;
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
			// for(byte x : bytes){
				// System.out.print("<"+x+">");
			// }
			// System.out.println("end");
			LZ77.compress(bytes);
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
	private final static int dictlength = 4096;
	private final static int lookuplength = 1024;
	public static void compress(byte[] data){
		ArrayList<Pair<Pair<Short, Short>, Byte>> output = new ArrayList<Pair<Pair<Short, Short>, Byte>>();
		int n = data.length-1;
		int l=0, r = n, dl=0;
		if(n > lookuplength)
			r = lookuplength;
		while(l < n){
			// System.out.println(dl+" "+l+" "+r);
			Pair<Short, Short> tmp = findmatch(data, dl, l, r);
			output.add(new Pair<Pair<Short, Short>, Byte> (tmp, data[(int) l+tmp.getSecond()]));
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
	private static Pair<Short, Short> findmatch(byte[] data, int dl, int l, int r){
		int len=0, idx=0;
		for(int i=dl, j;i<l;++i){
			for(j=0;j<r-l && j<l-i;++j){
				if(data[j+i]!=data[j+l])
					break;
			}
			if(j > len){
				len = j;
				idx = l-i;
				if(len > l-i-1)
					break;
			}
		}
		return new Pair<Short, Short>((short)idx, (short)len);
	}
	private static class Pair <T, U>{
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
}