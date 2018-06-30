import java.util.*;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.*;

public class Compressed {
	private String data;
	private String prevName;
	public Compressed(){
		
	}
	public String toBytes(){
		return prevName+"\n"+data;
	}
	public void setPrevName(String nameFile) {
		prevName = nameFile;
	}
	public void setData(String result) {
		data = result;
	}


	public Compressed doCompress(String nameFile){
		Compressed compressed = new Compressed();
		compressed.setPrevName(nameFile);

		try {
            // Create a file path of the file
            Path filePath = Paths.get(nameFile);

            // Reading all bytes in a file into a string
            String stringData = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        	
        	// compressing
        	List<Long> compressedData = compress_LZW(stringData);

        	StringBuilder strbul  = new StringBuilder();
		    Iterator<Long> iter = compressedData.iterator();
		    while(iter.hasNext()){
		        strbul.append(iter.next());
		        if(iter.hasNext()){
		         	strbul.append(",");
		        }
		    }
			compressed.setData(strbul.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


		return compressed;
	}
	public void decompress(String nameFile){
		String fileName;
		String compressedString;
		try {
            // Create a file path of the file
            System.out.println(nameFile);
            Path filePath = Paths.get(nameFile);

            // Reading all bytes in a file into a string
            String stringData = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);

			String[] parts = stringData.split("\n");
			fileName = parts[0]; 
			compressedString = parts[1]; 

			// fill the compressedString back to List<Long> 
	        List<Long> symbolTable = new ArrayList<Long>();
	        String[] symbolTableRaw = compressedString.split(",");
			for (String c : symbolTableRaw) {
	            symbolTable.add(Long.parseLong(c));
	        }

			// Decompressing
			String decompressed = decompress_LZW(symbolTable);

			// Writing to output file			
			FileOutputStream fos = null;
			DataOutputStream dos = null;
			try {
				
				fos = new FileOutputStream(fileName);
				
				dos = new DataOutputStream(fos);
				 
				dos.writeBytes(decompressed);
				
			}
			catch (FileNotFoundException fnfe) {
				System.out.println("File not found" + fnfe);
			}
			catch (IOException ioe) {
				System.out.println("Error while writing to file" + ioe);
			}
			finally {
				try {
					if (dos != null) {
						dos.close();
					}
					if (fos != null) {
						fos.close();
					}
				}
				catch (Exception e) {
					System.out.println("Error while closing streams" + e);
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }      

		return;
	}

	/* YOU CAN ADD ANOTHER METHOD OR VARIABLE BELOW HERE */
	/* Generating the LZW Dictionary as List of Long Integer */
    public static List<Long> compress_LZW(String uncompressed) {
        // Build the base dictionary.
        long dictSize = 256;
        Map<String,Long> dictionary = new HashMap<String,Long>();
        for (long i = 0; i < 256; i++)
            dictionary.put("" + (char)i, i);
 
 		// Adding the unique prefix to the dictionary
        String w = "";
        List<Long> result = new ArrayList<Long>();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                result.add(dictionary.get(w));
                // Add wc to the dictionary.
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }
 
        // Output the code for w.
        if (!w.equals(""))
            result.add(dictionary.get(w));
        return result;
    }
 
    /** Translating the Dictionary into String */
    public static String decompress_LZW(List<Long> compressed) {
        // Build the base dictionary.
        long dictSize = 256;
        Map<Long,String> dictionary = new HashMap<Long,String>();
        for (long i = 0; i < 256; i++)
            dictionary.put(i, "" + (char)i);
 
 		// Build the rest by the first one
        String w = "" + (char)(long)compressed.remove(0);
        StringBuffer result = new StringBuffer(w);
        for (Long k : compressed) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Can not compressed k: " + k);
 
            result.append(entry);
 
            // Adding prefix as w+entry[0] to the dictionary 
            dictionary.put(dictSize++, w + entry.charAt(0));
 
            w = entry;
        }
        return result.toString();
    }
};
