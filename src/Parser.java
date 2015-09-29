import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.filter.*;

public class Parser {

	static Document timeline;
	static Element racine;
	public static void parse(String timelinePath, String blackPath){
		SAXBuilder sxb = new SAXBuilder();
		File timelineFile;
		try {
			
			timelineFile = new File(timelinePath);
			timeline = sxb.build(timelineFile);
			racine =  timeline.getRootElement();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error Occured");
			System.out.println(e);
			return;
		}
		
		List<Element> trackList = racine.getChild("trackList", racine.getNamespace()).getChildren("track", racine.getNamespace());
		
		File blackFile = new File(blackPath);
		
		Iterator<Element> tracks = trackList.iterator();
		int compteur = 0;
		while (tracks.hasNext()){
			Element currentTrack = tracks.next();
			
			//ignore tracks in the playlist which aren't videos
			if (currentTrack.getChildText("duration", racine.getNamespace())==null){
				System.out.println("skipped one track (no duration)");
				continue;
			}
		
			try {
				
				
				String path = URLDecoder.decode(currentTrack.getChildText("location", racine.getNamespace()).substring(8), "UTF-8");
				path = path.replace('/', '\\');
				String goodPath = findFile(path);
				if (goodPath.equals("null")){
					System.out.println("le fichier à l'adresse " + path +" est introuvable.");
					continue;
				}
				
				File originalclip = new File(goodPath);
				
				//checks if the track doesn't correspond to a black cut
				if (resetFilename(originalclip.getName()).equals(resetFilename(blackFile.getName()))) continue;
				
				File destclip = new File(insertDigitPath(goodPath, compteur,'a'));
				if (!destclip.exists()) originalclip.renameTo(destclip);
				
				
				//if there is an srt file with the movie
				String srtPath = subtitlePath(goodPath);
				File originalsrt = new File(srtPath);
				if (originalsrt.exists()){
					File destsrt = new File(insertDigitPath(srtPath, compteur, 'a'));
					originalsrt.renameTo(destsrt);
				}
				
				//add a black clip if necesserary
				String blackTestPath = insertDigitPath(destclip.getParent()+"\\"+blackFile.getName(), compteur, 'b');
				File blackTestFile = new File(blackTestPath);
				if (!blackTestFile.exists()){
					try {
						Files.copy(blackFile.toPath(), blackTestFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				System.out.println("OK : " + goodPath);
				compteur++;
			
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
		}
		
		
		
		
	}
	
	/**
	 * if the file doesn't exist at the given path, the function searches for 
	 * files with a prefix of three numbers
	 * @param path
	 * @return the correct filepath if the file is found, or "null" otherwise
	 */
	static String findFile(String path){
		File file = new File(path);
		
		if (file.exists()) return path;
		else {
			int splitIndex = 0;
			for(int i=path.length()-1; i>=0; i--){
				if (path.charAt(i) == '\\') {
					splitIndex = i;
					break;
				}
			}
			String folder = path.substring(0,splitIndex+1);
			String filename = path.substring(splitIndex+1);
			filename = resetFilename(filename);
			file = new File(folder+filename);
			if (file.exists()) 	return file.getAbsolutePath();
			for (int i = 0; i<1000; i++){
				file = new File(folder+digits(i)+"a_"+filename);
				if (file.exists()) 	return file.getAbsolutePath();
			}
		}
		return "null";
	}
	static String digits(int i){
		if (i<10) return "00" + i;
		if (i<100) return "0" + i;
		return i+"";
	}
	static String resetFilename(String filename){
		int i=0;
		if (filename.length()<5) return filename;
		while(i<3){
			if (!Character.isDigit(filename.charAt(i))) return filename;
			i++;
		}
		if (filename.charAt(3)!='a' && filename.charAt(3)!='b') return filename;
		if (filename.charAt(4)!='_') return filename;
		return filename.substring(5);
	}
	
	static String insertDigitPath(String path, int compteur, char c){
		int splitIndex = 0;
		for(int i=path.length()-1; i>=0; i--){
			if (path.charAt(i) == '\\') {
				splitIndex = i;
				break;
			}
		}
		return path.substring(0,splitIndex+1)+digits(compteur) + c +'_' +resetFilename(path.substring(splitIndex+1));
	}
	
	static String subtitlePath(String moviePath){
		int i = moviePath.length()-1;
		while(moviePath.charAt(i)!='.') i--;
		return moviePath.substring(0,i+1)+"srt";
	}
}
