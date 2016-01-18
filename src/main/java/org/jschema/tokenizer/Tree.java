package org.jschema.tokenizer;

/**
 * Created by sachin on 1/14/16.
 */
public class Tree {
    public Node root;

    public Tree() {

    }

    public void print() {
        root.print(0);
    }
}