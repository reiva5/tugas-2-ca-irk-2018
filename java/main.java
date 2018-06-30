import java.lang.*;
import java.util.*;
import java.io.*;

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
			String tmpFile = args[0];
			String fileName = "";
			for (int i = tmpFile.length()-1; i >= 0; --i){
				if (tmpFile.charAt(i) == '.'){
					for (int j = 0; j < i; ++j){
						fileName += tmpFile.charAt(j);
					}
				}
			}
			fileName += ".irk";
			
			try {
				File logFile = new File(fileName);
	            FileWriter log_file_writer = new FileWriter(logFile);
	            log_file_writer.write(tmp.toBytes());
	            log_file_writer.flush();
	            log_file_writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			System.out.println("Ukuran hasil kompresi adalah: " + calc.sizeOf(tmp)/8 + " byte(s)");
			System.out.println("Eksekusi waktu hasil kompresi adalah: " + String.format("%.3f",(double) (end - begin)/1000000000.0) + " second(s)");
		} else {
			long begin = System.nanoTime();
			tmp.decompress(args[0]);
			long end = System.nanoTime();
			System.out.println("Eksekusi waktu hasil de-kompresi adalah: " + String.format("%.3f",(double) (end - begin)/1000000000.0) + " second(s)");
		}

	}
}
