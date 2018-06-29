import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class main_old{
	private static ArrayList<Integer> integersArray;
	private static String noExtName;
	private static String extName;
	
	public static byte getBit(byte b,int position)
	{
	   return (byte) ((b >> position) & 1);
	}
	public static int unsignedByteToInt(byte b) {
		return 0x00 << 24 | b & 0xff;
    }
	public static void fillFileName(String namaFile){
		String[] temp = namaFile.split("\\.");
		noExtName = temp[0];
		extName = temp[1];
	}
	public static void main(String args[]){
		//read file
		System.out.print("Input file name to be compressed : ");
		Scanner scanner = new Scanner(System. in);
		String namaFile = scanner. nextLine();
		fillFileName(namaFile);
		
		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;
		try{
			System.out.println("Processing file ...");
			File file = new File(namaFile);
			bytesArray = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);
			try{
			Thread.sleep(1000);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			System.out.println("File processed.\nFile name : " + namaFile + "\nFile size : " + file.length() + " bytes");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if (fileInputStream != null){
				try{
					fileInputStream.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		//create base symbols
		ArrayList<ArrayList<Integer>> symbols = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < 256; ++i){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(i);
			symbols.add(temp);
		}
		System.out.println("Compressing ...");
		//process file and encode + make dict
		integersArray = new ArrayList<Integer>();
		if(bytesArray.length != 0){
			int i = 0;
			ArrayList<Integer> s = new ArrayList<Integer>();
			s.add(unsignedByteToInt(bytesArray[i++]));
			while(i < bytesArray.length){
				int c = unsignedByteToInt(bytesArray[i]);
				s.add(c);
				if(!symbols.contains(s)){
					if(symbols.size() < 65536){
						ArrayList<Integer> copy = new ArrayList<Integer>();
						for(Integer num : s){
							copy.add(new Integer(num));
						}
						symbols.add(copy);
					}
					s.remove(s.size() - 1);
					integersArray.add(symbols.indexOf(s));
					s.clear();
					s.add(c);
				}
				i++;
			}
			integersArray.add(symbols.indexOf(s));
		}
		System.out.println("Packing ...");
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		//packing(encode) short and byte
		int k = 0;
		byte b1 = 0;
		ArrayList<Byte> tempB = new ArrayList<Byte>();
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		try{
			byte[] byteExt = extName.getBytes("US-ASCII");
			for(byte bz : byteExt){
				bytes.add(bz);
			}
			bytes.add((byte) 0);
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}

		for(int num : integersArray){
			if(num < 256){
				b1 = (byte) (b1 << 1);
				tempB.add((byte) num);
			}
			else{
				b1 = (byte) (b1 << 1 | 1);
				tempB.add((byte) (num >> 8));
				tempB.add((byte) num);
			}
			k++;
			if(k == 8){
				k = 0;
				bytes.add(b1);
				for(byte bt : tempB){
					bytes.add(bt);
				}
				b1 = 0;
				tempB = new ArrayList<Byte>();
			}
		}
		if(integersArray.size() % 8 != 0){
			int rem = integersArray.size() % 8;
			b1 <<= 8 - rem;
			bytes.add(b1);
			for(byte bt : tempB){
				bytes.add(bt);
			}
		}
		
		System.out.println("Writing to destination file ...");
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		//write to compressed file
		FileOutputStream fileOutputStream = null;
		try{
			File file = new File(noExtName + ".irk");
			fileOutputStream = new FileOutputStream(file);
			DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
			for(byte bz : bytes){
				dataOutputStream.write(bz);
				dataOutputStream.flush();
			}
			System.out.println("Compression finished!\nFile name : " + noExtName + ".irk\nFile size : " + file.length() + " bytes\n");
			dataOutputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if (fileOutputStream != null){
				try{
					fileOutputStream.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		System.out.println("WARNING : AUTOMATIC DECOMPRESSING " + noExtName + ".irk");
		System.out.println("EXIT TO ABORT");
		try{
			Thread.sleep(5000);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("Decompressing " + noExtName + ".irk ...");
		//read compressed file
		integersArray = new ArrayList<Integer>();
		bytes = new ArrayList<Byte>();
		fileInputStream = null;
		try{
			File file = new File(noExtName + ".irk");
			fileInputStream = new FileInputStream(file);
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			extName = "";
			byte temp = dataInputStream.readByte();
			while(temp != 0){
				extName += (char) temp;
				temp = dataInputStream.readByte();
			}
			
			while(dataInputStream.available() > 0){
				bytes.add(dataInputStream.readByte());
			}
			dataInputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if (fileInputStream != null){
				try{
					fileInputStream.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		System.out.println("Unpacking ...");
		//unpack
		integersArray = new ArrayList<Integer>();
		int cnt1 = 0;
		while(cnt1 < bytes.size()){
			byte len = bytes.get(cnt1++);
			for(int cnt2 = 0; cnt2 < 8; ++cnt2){
				if(getBit(len, 7 - cnt2) == 0){
					if(cnt1 < bytes.size()){
						int num = unsignedByteToInt(bytes.get(cnt1++));
						integersArray.add(num);
					}
				}
				else{
					int num = unsignedByteToInt((byte) bytes.get(cnt1++)) << 8;
					num += unsignedByteToInt(bytes.get(cnt1++));
					integersArray.add(num);
				}
			}
		}
		
		
		//process file and decode
		if(integersArray.size() != 0){
			symbols = new ArrayList<ArrayList<Integer>>();
			for(int i = 0; i < 256; ++i){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(i);
				symbols.add(temp);
			}
			
			ArrayList<Byte> bytesArrayL = new ArrayList<Byte>();
			fileOutputStream = null;
			try{
				File file = new File(noExtName + "." + extName);
				fileOutputStream = new FileOutputStream(file);
				DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
				
				ArrayList<Integer> s = new ArrayList<Integer>();
				int i = 0;
				int enc1 = integersArray.get(i++);
				int c = 0;
				bytesArrayL.add((byte) enc1);
				while(i < integersArray.size()){
					int enc2 = integersArray.get(i);
					if(enc2 < symbols.size()){
						s.clear();
						for(int num : symbols.get(enc2)){
							s.add(num);
						}
					}
					else{
						s.clear();
						for(int num : symbols.get(enc1)){
							s.add(num);
						}
						s.add(c);
					}
					for(int num : s){
						bytesArrayL.add((byte) num);
					}
					c = s.get(0);
					ArrayList<Integer> temp = new ArrayList<Integer>();
					for(int num : symbols.get(enc1)){
						temp.add(num);
					}
					temp.add(c);
					symbols.add(temp);
					enc1 = enc2;
					i++;
				}
				for(byte b : bytesArrayL){
					dataOutputStream.write(b);
				}
				System.out.println("Decompression finished!\nFile name : " + noExtName + "." + extName + "\nFile size : " + file.length() + " bytes");
				dataOutputStream.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			finally{
				if (fileOutputStream != null){
					try{
						fileOutputStream.close();
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
}
