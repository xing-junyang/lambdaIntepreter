import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String s;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter expression (or 'exit' to exit): ");
            s = br.readLine();
            if (s.equals("exit")) {
                break;
            }
            Lexer lexer = new Lexer(s);
            Parser parser = new Parser(lexer);
            Interpreter interpreter = new Interpreter(parser);
            AST result = interpreter.eval();
            if(result == null|| result.toString()==null){
                System.out.println("Wrong input!");
                continue;
            }else{
                System.out.println("After parser:" + result.toString());
            }
        }
    }
}
