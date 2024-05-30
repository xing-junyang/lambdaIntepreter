

public abstract class AST {
    boolean isValue = false;
    public abstract String toString();
    public abstract boolean equals(AST ast);
}
