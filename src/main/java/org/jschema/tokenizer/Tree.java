package org.jschema.tokenizer;


public class Tree {
    public Node root;

    public Tree() {

    }

    public void print() {
        root.print(0);
    }
}