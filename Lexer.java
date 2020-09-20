import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
public class Lexer {
	Pattern pattern = Pattern.compile("(else|if|int|return|void|while)|([a-zA-Z]+)|([0-9]+)|(==|!=|<=|>=|<|>|=|;|,|\\+|\\-|\\/|\\*|\\(|\\)|\\[|\\]|\\{|\\})|(\\S+)");
	String multiLineCommentButOnlyOneLine = "(\\/\\*).*(\\*\\/)";
	String multiLineCommentBeginning = "(\\/\\*).*";
	String multiLineCommentEnding = "^(.*?)(\\*\\/)";
	String oneLineComment = "(\\/\\/).*";
	static ArrayList<String> tobecomeparsabletokens = new ArrayList<>();
	boolean comment = false;
	public Lexer(File textfile) throws FileNotFoundException {
		try {
			Scanner scanner = new Scanner(textfile);
			while(scanner.hasNext() ) {
				String line = scanner.nextLine().trim();
				if(line.length() == 0) {
					continue;
				}
				line = line.replaceAll(oneLineComment, "");
				if(line.contains("/*")) {
					if(line.contains("*/")) {
						line = line.replaceAll(multiLineCommentButOnlyOneLine, "");
						comment = false;
					}
					line = line.replaceAll(multiLineCommentBeginning, "");
					comment = true;
				} else if(line.contains("*/")) {
					if(comment) {
						line = line.replaceAll(multiLineCommentEnding, "");
						comment = false;
					}
				}
				
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()) {
					if(matcher.start(1) != -1) { // keywords 
						tobecomeparsabletokens.add(matcher.group());
					} else if(matcher.start(2) !=  -1) { // identifiers 
						tobecomeparsabletokens.add("ID");
					} else if(matcher.start(3) != -1) { // numbers 
						tobecomeparsabletokens.add("NUM");
					} else if(matcher.start(4) != -1) { // special symbols 
						tobecomeparsabletokens.add(matcher.group());
					}  else if(matcher.start(5) != -1) {
						tobecomeparsabletokens.add("ERROR"); // errors  
					}
				}
				
				
				
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<String> getArrayList() {
		return tobecomeparsabletokens;
	}
}
