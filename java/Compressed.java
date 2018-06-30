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
		nameTemp = "";
		if (bytesArray.length != 0) {
			byte temp = bytesArray[0];
			int count = 1;
			for (int i=1; i<bytesArray.length; i++) {
				String input = new String(new byte[] {temp});
				nameTemp += input;
				if (temp == bytesArray[i]) {
					count++;
				} else {
					if (count == 1) {
						data = data + input;
					} else if ((count == 2) || (count == 3)){
						data += nameTemp;
						count = 1;
					} else {
						data = data + input + "`[" + count;
						count = 1;
					}
					nameTemp = "";
					temp = bytesArray[i];
				}
				if (i == bytesArray.length - 1) {
					if (count == 1) {
						input = new String(new byte[] {temp});
						data = data + input;
					} else {
						data = data + input + "`[" + count;
					}
				}
			}
		}
		return this;
	}
	public void decompress(String nameFile){
		// Read File
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
		data = new String(bytesArray);
		//
		
		String fileName = "";
		for (int i = nameFile.length()-1; i >= 0; --i){
			if (nameFile.charAt(i) == '.'){
				for (int j = 0; j < i; ++j){
					fileName += nameFile.charAt(j);
				}
			}
		}
		fileName += '.';
		for (int i=1; i<=Character.getNumericValue(data.charAt(0)); i++) {
			fileName += data.charAt(i);
		}
		String newData = "";
		for (int i=Character.getNumericValue(data.charAt(0)) + 1; i<data.length(); i++) {
			newData += data.charAt(i);
		}
		
		data = "";
		int count = 0;
		char temp = newData.charAt(0);
		for (int i=0; i<newData.length(); i++) {
			if (count == 3) {
				count = 0;
				temp = newData.charAt(i);
			}
			if (count == 2) {
				for (int j=1; j<Character.getNumericValue(newData.charAt(i)); j++) {
					data += temp;
				}
				count = 3;
			}
			if (count == 0) {
				if (newData.charAt(i) == '`') {
					count++;
				} else {
					data += newData.charAt(i);
					temp = newData.charAt(i);
				}
			} else if (count == 1) {
				if (newData.charAt(i) == '[') {
					count++;
				} else {
					count = 0;
					temp = newData.charAt(i);
					data += '`' + newData.charAt(i);
				}
			} 
		}
		
		byte[] newByte = data.getBytes();
		
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(fileName);
			stream.write(newByte);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}
}