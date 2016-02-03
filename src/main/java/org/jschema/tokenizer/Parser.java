package org.jschema.tokenizer;

import java.util.ArrayList;
import java.util.List;
import static org.jschema.tokenizer.Token.TokenType.*;


public class Parser {
    public Parser() {

    }

    public Tree parse(String json) {
        List<Token> tokens = new Tokenizer(json).tokenize();

        Node root = new Node();
        root.value = new Token(STRING, "ROOT", 0, 0, 0);

        Tree tree = new Tree();
        tree.root = root;

        Node currentParent = root;
        for (Token token : tokens) {
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
        }

        tree.print();
        return tree;
    }
}
