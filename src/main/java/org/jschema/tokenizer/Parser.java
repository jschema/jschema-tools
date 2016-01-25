package org.jschema.tokenizer;

import static org.jschema.tokenizer.Token.TokenType.*;

/**
 * Created by Sachin Patel on 1/14/16.
 */

public class Parser {
    public Parser() {

    }
    public Tree parse(String json) {
        Tokenizer tokenizer = new Tokenizer(json);
        Node root = new Node();
        root.value = new Token(STRING, "ROOT", 0, 0, 0, 0);

        Tree tree = new Tree();
        tree.root = root;

        Node currentParent = root;
        Token token = tokenizer.next();
        while(token.getTokenType() != EOF) {
            Node node = new Node();
            node.value = token;
            node.parent = currentParent;
            currentParent.children.add(node);

            if (token.getTokenType() == PUNCTUATION) {
                if (token.getTokenValue().equals("{")) {
                    currentParent = node;
                } else if (token.getTokenValue().equals("}")) {
                    currentParent = currentParent.parent;
                }
            }
            token = tokenizer.next();
        }

        tree.print();
        return tree;
    }
}
