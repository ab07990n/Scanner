
/**
 * @author scharffc
 * 
 */
public class ScannerDemo {

	private static String file1 = "prog1.jay";
	private static int counter = 1;

	public static void main(String args[]) {
		String fileName = (args.length > 0) ? args[0] : file1;
		TokenStream ts = new TokenStream(fileName);
		System.out.println(fileName);
		while (!ts.isEndofFile()) {
			Token tk = ts.nextToken();
			if (!tk.getValue().isEmpty() || !ts.isEndofFile()) {
				System.out.println("Token " + counter++ + " - Type: " + tk.getType() + " - Value: " + tk.getValue());
			}
		}
	}
}
