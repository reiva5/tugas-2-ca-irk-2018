import java.lang.*;

public class main {
	public static Compressed tmp;
	public static ObjectSizeCalculator calc;

	public static void main(String[] args) throws Exception {
		calc = new ObjectSizeCalculator();
		String query = args[1];
		tmp = new Compressed();
		if (query.equals("1")){
			long begin = System.nanoTime();
			tmp = tmp.doCompress(args[0]);
			long end = System.nanoTime();
			System.out.println("Ukuran hasil kompresi adalah: " + calc.sizeOf(tmp)/8 + " byte(s)");
			System.out.println("Eksekusi waktu hasil kompresi adalah: " + String.format("%.3f",(double) (end - begin)/1000000000.0) + " second(s)");
		} else {
			long begin = System.nanoTime();
			tmp.decompress(args[1]);
			long end = System.nanoTime();
			System.out.println("Eksekusi waktu hasil de-kompresi adalah: " + String.format("%.3f",(double) (end - begin)/1000000000.0) + " second(s)");
		}

	}
}