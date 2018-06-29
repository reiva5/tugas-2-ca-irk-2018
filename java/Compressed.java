import java.lang.*;
import java.util.*;
import java.io.*;

public class Compressed {
	private String data;
	public Compressed(){
		data = "";
	}
	
	public String toBytes(){
		return data;
	}

	public Compressed doCompress(String nameFile){
		int check = 0;
		String nameTemp = "";
		for (int i = 0; i < nameFile.length(); i++){
			if (check > 0) {
				check++;
				nameTemp += nameFile.charAt(i);
			} else {
				if (nameFile.charAt(i) == '.'){
					check = 1;
				}
			}
		}
		check--;
		data += check + nameTemp;
		
		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;
		
		try {
			File file = new File(nameFile);
			bytesArray = new byte[(int) file.length()];
			
			// Read File into bytes[]
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//Run Length Algorithm
		if (bytesArray.length != 0) {
			byte temp = bytesArray[0];
			int count = 1;
			for (int i=1; i<bytesArray.length; i++) {
				String input = new String(new byte[] {temp});
				if (temp == bytesArray[i]) {
					count++;
				} else {
					if (count == 1) {
						data = data + input;
					} else {
						data = data + input + '`' + count;
						count = 1;
					}
					temp = bytesArray[i];
				}
				if (i == bytesArray.length - 1) {
					data = data + input + count;
				}
			}
		}
		return this;
	}
	public void decompress(String nameFile){
		return;
	}
}