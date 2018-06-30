public class Compressed2 {
	private String nameFile = "Asu";
	public Compressed(){
		nameFile = "susu";
	}
	public String toBytes(){
        
        String bForm = "";

        File file = new File(nameFile);

        byte[] b = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(b);
        for (int i = 0; i < b.length; i++) {
            bForm += (char)b[i];
        }
		return bForm;
	}
	public Compressed doCompress(String nameFile){
		Compressed compressed = new Compressed();
		return compressed;
	}
	public void decompress(String nameFile){
		return;
	}
	/* YOU CAN ADD ANOTHER METHOD OR VARIABLE BELOW HERE */
};