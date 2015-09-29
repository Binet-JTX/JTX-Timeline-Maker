import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.filter.*;


public class Main {
	
	private String noir = "";
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 2){
			Parser.parse(args[0], args[1]);
		}
		
		else {
			GUI gui = new GUI();
		}
	}

}
