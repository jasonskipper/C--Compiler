import java.io.FileNotFoundException;
import java.lang.Object;
import java.io.File;
import java.util.ArrayList;
public class Parser {
	
	static ArrayList<String> tokensforparser = new ArrayList<>();
	static boolean ishouldaccept;
	static int index = 0;
	
	public static void main(String[] args) throws FileNotFoundException {
		try {
			if(args.length < 1) {
				System.exit(1);
			}
			File file = new File(args[0]);
			if(!file.isFile()) {
				System.exit(1);
			}
			//File file = new File("filenameA.txt");
 			Lexer lexer = new Lexer(file);
			tokensforparser = lexer.getArrayList(); // now we have our list of tokens 
			ishouldaccept  = true;
			Program();
			for(int i = 0; i < tokensforparser.size(); i++) {
				System.out.println(tokensforparser.get(i));
			}
			if(index >= tokensforparser.size()-1) {
				if(ishouldaccept) {
					System.out.println("ACCEPT");
				} else {
					System.out.println("REJECT");
				}
			} else {
				System.out.println("REJECT");
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	} // end of main 
// } // fake 
	
	// ---------- now here's the parser we've all been waiting for: 
	
	// 1. program -> declarationList 
	public static void Program() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			declarationList();
		} else {
			ishouldaccept = false;
		}
	}
	// 2A. declarationList -> declaration declarationListPrime  
	public static void declarationList() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			declaration();
			if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
				declarationListPrime();
			} // empty is in first of declarationListPrime 
		} else {
			ishouldaccept = false;  
		}
	}
	// 2B. declarationListPrime -> declaration declarationListPrime | empty 
	public static void declarationListPrime() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			declaration();
			if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
				declarationListPrime();
			} // empty in declarationlistprime 
		} // empty 
	}
	// 3A. declaration -> typeSpecifier ID declarationPrime 
	public static void declaration() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			typeSpecifier();
			if(tokensforparser.get(index) == "ID") {
				index++;
				if(tokensforparser.get(index) == ";" || tokensforparser.get(index) =="[") {
					declarationPrime();
				} else {
					ishouldaccept = false;
				}
			} else {
				ishouldaccept = false;
			}
		} else {
			ishouldaccept = false;
		}
	}
	// 3B. declarationPrime -> varDeclarationPrime | (params) compoundStmt; 
	public static void declarationPrime() {
		if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "[") {
			varDeclarationPrime();
		} else if(tokensforparser.get(index) == "(") {
			index++;
			if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
				params();
				if(tokensforparser.get(index) == ")") {
					index++;
					if(tokensforparser.get(index) == "{") { 
						compoundStmt();
					} else ishouldaccept = false;
				} else ishouldaccept = false; 
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	// 4A. varDeclaration -> typeSpecifier ID varDeclarationPrime 
	public static void varDeclaration() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			typeSpecifier();
			if(tokensforparser.get(index) == "ID") {
				index++;
				if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "[") {
					varDeclarationPrime();
				} else ishouldaccept = false;
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	// 4B. varDeclarationPrime -> ; | [NUM];  
	public static void varDeclarationPrime() {
		if(tokensforparser.get(index) == ";") {
			index++;
		} else if(tokensforparser.get(index) == "[") {
			index++;
			if(tokensforparser.get(index) == "NUM") {
				index++;
				if(tokensforparser.get(index) == "]") {
					index++;
					if(tokensforparser.get(index) ==";") {
						index++;
					} else ishouldaccept = false; 
				} else ishouldaccept = false;
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	// 5. typeSpecifier -> int | void 
	public static void typeSpecifier() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			index++;
		} else ishouldaccept = false;
	}
	// 6. funDeclaration -> typeSpecifier ID (params) compoundStmt; 
	public static void funDeclaration() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			typeSpecifier();
			if(tokensforparser.get(index) == "ID") {
				index++;
				if(tokensforparser.get(index) == "(") {
					index++;
					if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
						params();
						if(tokensforparser.get(index) == ")") {
							index++;
							if(tokensforparser.get(index) == "{") {
								compoundStmt();
								if(tokensforparser.get(index) == ";") {
									index++;
								} else ishouldaccept = false; 
							} else ishouldaccept = false;
						} else ishouldaccept = false;
					} else ishouldaccept = false;
				} else ishouldaccept = false;
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	
	// 7A. params -> int ID param-list-prime | void params-prime
	public static void params() {
		if(tokensforparser.get(index) == "int") {
			index++;
			if(tokensforparser.get(index) == "ID") {
				index++;
				if(tokensforparser.get(index) == ",") {
					// first of paramlistprime is , or empty 
					paramListPrime();
				} // empty 
			} else ishouldaccept = false; 
		} else if(tokensforparser.get(index) == "void") {
			index++;
			if(tokensforparser.get(index) == "ID") {
				paramsPrime();
			} // empty in paramsprime 
		} else ishouldaccept = false;
	}
	// 7B. paramsPrime -> ID param-list-prime | empty 
	public static void paramsPrime() {
		if(tokensforparser.get(index) == "ID") {
			index++;
			if(tokensforparser.get(index) == ",") {
				paramListPrime();
			} // empty in first set of paramListPrime 
		} // empty  
	}
	// 8A. paramList -> param paramListPrime 
	public static void paramList() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			param();
			if(tokensforparser.get(index) == ",") {
				paramListPrime();
			} // empty in first set of paramListPrime 
		} else ishouldaccept = false;
	}
	// 8B. param-list-prime -> ,  param param-list-prime | empty  
	public static void paramListPrime() {
		if(tokensforparser.get(index) == ",") {
			index++;
			if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
				param();
				if(tokensforparser.get(index) == ",") {
					paramListPrime();
				} // empty in 1st set of paramListPrime 
			} else ishouldaccept = false;
		} // empty 
	}
	// 9A. param -> type-specifier ID param-prime
	public static void param() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			typeSpecifier();
			if(tokensforparser.get(index) == "ID") {
				index++;
				if(tokensforparser.get(index) == "[") {
					paramPrime();
				} // empty in first set of paramPrime 
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	// 9B. paramPrime -> [] | empty 
	public static void paramPrime() {
		if(tokensforparser.get(index) == "[") {
			index++;
			if(tokensforparser.get(index) =="]") {
				index++;
			} else ishouldaccept = false;
		} // empty 
	}
	// 10. compound-stmt -> { local-declarations statement-list } 
	public static void compoundStmt() {
		if(tokensforparser.get(index) == "{") {
			index++;
			if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
				localDeclarations();
				if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == ";" ||
					tokensforparser.get(index) == "ID" ||tokensforparser.get(index) == "NUM" ||
					tokensforparser.get(index) == "if" ||tokensforparser.get(index) == "return" ||
					tokensforparser.get(index) == "while" || tokensforparser.get(index) == "{") {
					statementList();
					if(tokensforparser.get(index) == "}") {
						index++;
					} else ishouldaccept = false;
				} // empty in first set of statementList 
			} // empty in first set of localDeclarations 
		} else ishouldaccept = false;
	}
	// 11A. local-declarations -> local-declarations-prime 
	public static void localDeclarations() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			localDeclarationsPrime();
		} // empty in first set of localDeclarationsPrime 
	}
	// 11B. local-declarations-prime -> var-declaration local-declarations-prime | empty 
	public static void localDeclarationsPrime() {
		if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
			varDeclaration();
			if(tokensforparser.get(index) == "int" || tokensforparser.get(index) == "void") {
				localDeclarationsPrime();
			} // empty in first set of localDeclarationsPrime 
		} // empty  
	}
	
	// 12A. statementList -> statementListPrime 
	public static void statementList() {
		if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "NUM"
				|| tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" 
				|| tokensforparser.get(index) == "if" || tokensforparser.get(index) == "while"
				|| tokensforparser.get(index) == "return" || tokensforparser.get(index) == "{") {
			statementListPrime();
		} // empty in first of statementListPrime 
	}
	// 12B. statement-list-prime -> statement statement-list-prime | empty 
	public static void statementListPrime() {
		if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "NUM"
				|| tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" 
				|| tokensforparser.get(index) == "if" || tokensforparser.get(index) == "while"
				|| tokensforparser.get(index) == "return" || tokensforparser.get(index) == "{") {
			statement();
			if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "NUM"
					|| tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" 
					|| tokensforparser.get(index) == "if" || tokensforparser.get(index) == "while"
					|| tokensforparser.get(index) == "return" || tokensforparser.get(index) == "{") {
				statementListPrime();
			} // empty in first of statementListPrime 
		} // empty 
	}
	// 13. statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt | return-stmt 
	public static void statement() {
		if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == ";"
				|| tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
			expressionStmt();
			
		} else if(tokensforparser.get(index) == "{") {
			compoundStmt();
		} else if(tokensforparser.get(index) == "if") {
			selectionStmt();
		} else if(tokensforparser.get(index) == "while") {
			iterationStmt();
		} else if(tokensforparser.get(index) == "return") {
			returnStmt();
		} else ishouldaccept = false;
	}
	
	// 14. expressionStmt -> expression ; | ; 
	public static void expressionStmt() {
		if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "NUM" || tokensforparser.get(index) == "ID") { 
			expression();
			if(tokensforparser.get(index) == ";") {
				index++;
			} else ishouldaccept = false;
		} else if(tokensforparser.get(index) == ";") {
			index++;
		} else ishouldaccept = false;
	}
	
	// 15A. selection-stmt -> if ( expression ) statement selection-stmt-prime 
	public static void selectionStmt() {
		if(tokensforparser.get(index) == "if") {
			index++;
			if(tokensforparser.get(index) == "(") {
				index++;
				if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "NUM" || tokensforparser.get(index) == "ID") {
					expression();
					if(tokensforparser.get(index) == ")") {
						index++;
						if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "NUM"
								|| tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" 
								|| tokensforparser.get(index) == "if" || tokensforparser.get(index) == "while"
								|| tokensforparser.get(index) == "return" || tokensforparser.get(index) == "{") {
							statement();
							if(tokensforparser.get(index) == "else") {
								selectionStmtPrime();
							} // empty is in the first of selectionStmtPrime 
						} else ishouldaccept = false;
					} else ishouldaccept = false;
				} else ishouldaccept = false;
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	
	
	
	// 15B. selectionStmtPrime -> else statement | empty  
	public static void selectionStmtPrime() {
		if(tokensforparser.get(index) == "else") {
			index++;
			if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "NUM"
					|| tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" 
					|| tokensforparser.get(index) == "if" || tokensforparser.get(index) == "while"
					|| tokensforparser.get(index) == "return" || tokensforparser.get(index) == "{") {
				statement();
			} else ishouldaccept = false;
		} // empty 
	}
	
	// 16. iterationStmt -> while(expression) statement 
	public static void iterationStmt() {
		if(tokensforparser.get(index) == "while") {
			index++;
			if(tokensforparser.get(index) == "(") {
				index++;
				if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
					expression();
					if(tokensforparser.get(index) == ")") {
						index++;
						if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "NUM"
								|| tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" 
								|| tokensforparser.get(index) == "if" || tokensforparser.get(index) == "while"
								|| tokensforparser.get(index) == "return" || tokensforparser.get(index) == "{") {
							statement();
						} else ishouldaccept = false;
					} else ishouldaccept = false;
				} else ishouldaccept = false;
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	
	// 17A. returnStmt -> return returnStmtPrime
	public static void returnStmt() {
		if(tokensforparser.get(index) == "return") {
			index++;
			if(tokensforparser.get(index) == ";" || tokensforparser.get(index) == "ID" 
					|| tokensforparser.get(index) == "NUM" || tokensforparser.get(index) == "(") {
				returnStmtPrime();
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	
	// 17B. returnStmtPrime -> ; | expression ;  
	public static void returnStmtPrime() {
		if(tokensforparser.get(index) == ";") {
			index++;
		} else if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
			expression(); 
			if(tokensforparser.get(index) == ";") {
				index++;
			} else ishouldaccept = false; 
		} else ishouldaccept = false;
	}  
	
	// 18A. expression -> ID expression-prime 
					// | ( expression ) additive-expression-prime simple-expression-prime 
					// | NUM additive-expression-prime simple-expression-prime
	
	public static void expression() {
		if(tokensforparser.get(index) == "ID") {
			index++;
			if(tokensforparser.get(index) == "[") {
				expressionPrime();
			} // empty in first of expressionPrime  
		} else if(tokensforparser.get(index) == "(") {
			index++;
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
				expression(); 
				if(tokensforparser.get(index) == ")") {
					index++;
					if(tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
						additiveExpressionPrime();
						if(tokensforparser.get(index) == "<=" || tokensforparser.get(index) == "<"
								|| tokensforparser.get(index) == ">" || tokensforparser.get(index) == ">="
								|| tokensforparser.get(index) == "!=" || tokensforparser.get(index) == "==") {
							simpleExpressionPrime();
						} // empty in first of simpleExpressionPrime 
					} // empty in first of additiveExpressionPrime 
				} else ishouldaccept = false;
			} else ishouldaccept = false;
		} else if(tokensforparser.get(index) == "NUM") {
			index++;
			if(tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
				additiveExpressionPrime();
				if(tokensforparser.get(index) == "<=" || tokensforparser.get(index) == "<"
						|| tokensforparser.get(index) == ">" || tokensforparser.get(index) == ">="
						|| tokensforparser.get(index) == "!=" || tokensforparser.get(index) == "==") {
					simpleExpressionPrime();
				} // empty in first of simpleExpressionPrime 
			} // empty in first of additiveExpressionPrime
		} else ishouldaccept = false;
	}
	// 18B. expression-prime -> var-prime expression-prime-prime 
	public static void expressionPrime() {
		if(tokensforparser.get(index) == "[") {
			varPrime();
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
				expressionPrimePrime();
			} // empty in expressionprimeprime 
		} // empty in varprime 
	}
	// 18C. expression-prime-prime -> additive-expression-prime simple-expression-prime 
								// | (args) additive-expression-prime simple-expression-prime
	public static void expressionPrimePrime() {
		if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
			additiveExpressionPrime();
			if(tokensforparser.get(index) == "<=" || tokensforparser.get(index) == "<"
					|| tokensforparser.get(index) == ">" || tokensforparser.get(index) == ">="
					|| tokensforparser.get(index) == "!=" || tokensforparser.get(index) == "==") {
				simpleExpressionPrime();
			} // empty in first of simpleExpressionPrime
		} else if (tokensforparser.get(index) == "(") {
			
			
		} // empty in additiveexpressionprime 
		
	}
	// check empty before here!!!!  
	// 19A. var -> ID var-prime
	public static void var() {
		if(tokensforparser.get(index) == "ID") {
			index++;
			if(tokensforparser.get(index) == "[") {
				varPrime();
			} // varPrime goes to empty 
		} else ishouldaccept = false;
	}
	// 19B. var-prime -> [ expression ] | empty 
	public static void varPrime() {
		if(tokensforparser.get(index) == "[") {
			index++;
			if(tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "(" || tokensforparser.get(index) == "NUM") {
				expression();
				if(tokensforparser.get(index) == "]") {
					index++;
				} else ishouldaccept = false; 
			} else ishouldaccept = false;
		} // empty 
	}
	// 20A. simple-expression -> additive-expression simple-expression-prime 
	public static void simpleExpression() {
		if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
			additiveExpression();
			if(tokensforparser.get(index) == "<=" || tokensforparser.get(index) == "<"
					|| tokensforparser.get(index) == ">" || tokensforparser.get(index) == ">="
					|| tokensforparser.get(index) == "!=" || tokensforparser.get(index) == "==") {
				simpleExpressionPrime();
			} // simpleExpressionPrime goes to empty 
		} else ishouldaccept = false;
	}
	// 20B. simple-expression-prime -> relop additive-expression | empty 
	public static void simpleExpressionPrime() {
		if(tokensforparser.get(index) == "<=" || tokensforparser.get(index) == "<"
				|| tokensforparser.get(index) == ">" || tokensforparser.get(index) == ">="
				|| tokensforparser.get(index) == "!=" || tokensforparser.get(index) == "==") {
			relop();
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
				additiveExpression();
			} else ishouldaccept = false;
		} // empty 
	}
	// 21. relop -> <= | < | > | >= | == | != 
	public static void relop() {
		if(tokensforparser.get(index) == "<=" || tokensforparser.get(index) == "<"
				|| tokensforparser.get(index) == ">" || tokensforparser.get(index) == ">="
				|| tokensforparser.get(index) == "!=" || tokensforparser.get(index) == "==") {
			index++;
		}
	}
	// 22A. additive-expression -> term additive-expression-prime 
	public static void additiveExpression() {
		if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
			term();
			if(tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
				additiveExpressionPrime();
			} // empty 
		} else ishouldaccept = false;
	}
	// 22B. additive-expression-prime -> addop term additive-expression-prime | empty 
	public static void additiveExpressionPrime() {
		if(tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
			addop();
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
				term();
				if(tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
					additiveExpressionPrime();
				} // empty 
			} else ishouldaccept = false;
		} // empty 
	}
	// 23. addop -> + | - 
	public static void addop() {
		if(tokensforparser.get(index) == "+" || tokensforparser.get(index) == "-") {
			index++;
		} else ishouldaccept = false;
	}
	// 24A. term -> factor term-prime  
	public static void term() {
		
	}
	// 24B. term-prime -> mulop factor term-prime | empty 
	public static void termPrime() {
		if(tokensforparser.get(index) == "*" || tokensforparser.get(index) == "/") {
			mulop();
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
				factor();
				if(tokensforparser.get(index) == "*" || tokensforparser.get(index) == "/") {
					termPrime();
				} // empty 
			} else ishouldaccept = false;
		} // empty 
	}
	// 25. mulop -> * | / 
	public static void mulop() {
		if(tokensforparser.get(index) == "*" || tokensforparser.get(index) == "/") {
			index++;
		} else ishouldaccept = false;
	}
	// 26A. factor -> (expression) | ID factor-prime | NUM
	public static void factor() {
		if(tokensforparser.get(index) == "(") {
			index++;
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
				expression();
				if(tokensforparser.get(index) == ")") {
					index++;
				} else ishouldaccept = false; 
			} else ishouldaccept = false;
		} else if(tokensforparser.get(index) == "ID") {
			index++;
			if(tokensforparser.get(index) == "[" || tokensforparser.get(index) == "(") {
				factorPrime();
			} // empty in factorprime first set  
		} else if(tokensforparser.get(index) == "NUM") {
			index++;
		} else ishouldaccept = false;
	}
	// 26B. factor-prime -> var-prime | (args)  
	public static void factorPrime() {
		if(tokensforparser.get(index) == "[") {
			varPrime();
		} else if(tokensforparser.get(index) == "(") {
			index++;
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
				args();
				if(tokensforparser.get(index) == ")") {
					index++;
				} else ishouldaccept = false;
			} // empty 
		} // **** empty    
	}
	
	// 27. call -> ID ( args )  
	public static void call() {
		if(tokensforparser.get(index) == "ID") {
			index++;
			if(tokensforparser.get(index) == "(") {
				index++;
				if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
					args();
					if(tokensforparser.get(index) == ")") {
						index++;
					}
				} // empty 
			} else ishouldaccept = false;
		} else ishouldaccept = false;
	}
	// 28. args -> arg-list | empty 
	public static void args() {
		if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
			argList();
		} // empty 
	}
	// 29A. arg-list -> expression arg-list-prime 
	public static void argList() {
		if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
			expression();
			if(tokensforparser.get(index) == ",") {
				argListPrime();
			} // empty 
		} else ishouldaccept = false;
	}
	// 29B. arg-list-prime -> , expression arg-list-prime | empty 
	public static void argListPrime() {
		if(tokensforparser.get(index) == ",") {
			index++;
			if(tokensforparser.get(index) == "(" || tokensforparser.get(index) == "ID" || tokensforparser.get(index) == "NUM") {
				expression();
				if(tokensforparser.get(index) == ",") {
					argListPrime();
				} //  empty  
			} else ishouldaccept = false; 
		} // empty 
	}
	
	

} // end of parser  

