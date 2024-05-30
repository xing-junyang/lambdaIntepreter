public class Interpreter {
    Parser parser;
    AST astAfterParser;

    public Interpreter(Parser p) {
        parser = p;
        astAfterParser = p.parse();
    }

    private boolean isAbstraction(AST ast) {
        return ast instanceof Abstraction;
    }

    private boolean isApplication(AST ast) {
        return ast instanceof Application;
    }

    private boolean isIdentifier(AST ast) {
        return ast instanceof Identifier;
    }

    public AST eval() {
        return evalAST(astAfterParser);
    }


    private AST evalAST(AST ast) {
        while (true) {
            if (ast instanceof Application) {
                if (isAbstraction(((Application) ast).lhs)) {
                    ast = substitute(((Abstraction) ((Application) ast).lhs).body, ((Application) ast).rhs);
                } else if ((((Application) ast).lhs instanceof Application) && !(((Application) ast).rhs instanceof Identifier)) {
                    ((Application) ast).lhs = evalAST(((Application) ast).lhs);
                    ((Application) ast).rhs = evalAST(((Application) ast).rhs);
                    if (((Application) ast).lhs instanceof Abstraction) {
                        ast = evalAST(ast);
                    }
                    return ast;
                } else if (((Application) ast).lhs instanceof Application && ((Application) ast).rhs instanceof Identifier) {
                    ((Application) ast).lhs = (evalAST(((Application) ast).lhs));
                    if (((Application) ast).lhs instanceof Abstraction) {
                        ast = evalAST(ast);
                    }
                    return ast;
                } else {
                    ((Application) ast).rhs = (evalAST(((Application) ast).rhs));
                    return ast;
                }
            } else if (ast instanceof Abstraction) {
                ((Abstraction) ast).body = evalAST(((Abstraction) ast).body);
                return ast;
            } else {
                return ast;
            }
        }
    }

    private AST substitute(AST node, AST value) {
        return shift(-1, subst(node, shift(1, value, 0), 0), 0);
    }

    private AST subst(AST node, AST value, int depth) {
        if (node instanceof Application) {
            return new Application(subst(((Application) node).lhs, value, depth), subst(((Application) node).rhs, value, depth));
        } else if (node instanceof Abstraction) {
            return new Abstraction(((Abstraction) node).param, subst(((Abstraction) node).body, value, depth + 1));
        } else if (node instanceof Identifier) {
            if (depth == Integer.parseInt(((Identifier) node).value)) {
                return shift(depth, value, 0);
            } else {
                return node;
            }
        }
        return node;
    }

    private AST shift(int by, AST node, int from) {
        if (node instanceof Application) {
            return new Application(shift(by, ((Application) node).lhs, from), shift(by, ((Application) node).rhs, from));
        } else if (node instanceof Abstraction) {
            return new Abstraction(((Abstraction) node).param, shift(by, ((Abstraction) node).body, from + 1));
        } else if (node instanceof Identifier) {
            return new Identifier(((Identifier) node).name, Integer.toString(Integer.parseInt(((Identifier) node).value) + (Integer.parseInt(((Identifier) node).value) >= from ? by : 0)));
        }
        return node;
    }
}