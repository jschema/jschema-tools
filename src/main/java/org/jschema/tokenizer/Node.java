package org.jschema.tokenizer;

import java.util.ArrayList;

/**
 * Created by sachin on 1/14/16.
 */
public class Node {
    public Node parent;
    public ArrayList<Node> children;
    public Token value;

    public Node() {
        children = new ArrayList<Node>();
    }

    public void print(int depth) {
        Token.TokenType tokType = value.getTokenType();
        String tokValue = value.getTokenValue();

        if (tokType == Token.TokenType.PUNCTUATION && (tokValue.equals("{") || tokValue.equals("}") || tokValue.equals("[") || tokValue.equals("]") || tokValue.equals(","))) {
            if (tokValue.equals("}")) {
                System.out.println();
                System.out.print(tokValue);
            } else {
                System.out.print(tokValue);
                System.out.println();
            }

            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }
        } else {
            // Print value
            System.out.printf("%s", tokValue);

            if (tokValue.equals(":")) {
                System.out.print(" ");
            }
        }

        // Base case
        if (children.size() == 0) {
            return;
        }

        // Iterate over children
        for (Node child : children) {
            child.print(depth+1);
        }
    }
}