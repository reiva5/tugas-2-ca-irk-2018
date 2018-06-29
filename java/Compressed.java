public class Compressed {
	private ArrayList<Byte> dataByte;
	private String data;
	public Compressed(){
		dataByte = null;
		data = "";
	}
	
	public byte[] toBytes(){
		byte[] byteFile = new byte[dataByte.size()];
		for (int i=0; i<dataByte.size(); i++) {
			byteFile[i] = dataByte.get(i);
		}
		
		return byteFile;
	}
	
	public String toString() {
		return String(toBytes());
	}
	
	public Compressed doCompress(String nameFile){
		Compressed compressed = new Compressed();
		
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
		
		return compressed;
	}
	public void decompress(String nameFile){
		return;
	}
};