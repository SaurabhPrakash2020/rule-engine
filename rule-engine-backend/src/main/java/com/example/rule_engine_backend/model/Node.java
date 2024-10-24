package com.example.rule_engine_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testCollection") // Specify the collection name
public class Node {
    @Id // Unique identifier for MongoDB
    private String id;
    private String type; // "operator" or "operand"
    private Node left;   // Reference to left child
    private Node right;  // Reference to right child
    private String value; // Used for operands

    // Default constructor
    public Node() {}

    // Constructor for operands
    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Constructor for operators
    public Node(String type, Node left, Node right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if ("operand".equals(type)) {
            return value;
        }
        return "(" + left + " " + type + " " + right + ")";
    }
}
