package com.company;


//Used https://www.javatpoint.com/java-program-to-construct-a-binary-search-tree-and-perform-deletion-and-in-order-traversal

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree {

    //Represent a node of binary tree
    public static class Node{
        Double slope, intercept;
        Node left;
        Node right;

        public Node(Double slope, Double intercept){
            //Assign data to the new node, set left and right children to null
            this.slope = slope + 0.0; //have to add 0.0 to avoid -0.0 errors
            this.intercept = intercept + 0.0;
            this.left = null;
            this.right = null;
        }
    }

    //Represent the root of binary tree
    public Node root;

    public BinarySearchTree() {
        root = null;
    }

    //insert() will add new node to the binary search tree
    public boolean insert(Double slope, Double intercept) {
        //Create a new node
        Node newNode = new Node(slope, intercept);

        //Check whether tree is empty
        if(root == null){
            root = newNode;
            return false;
        }
        else {
            //current node point to root of the tree
            Node current = root, parent = null;

            while(true) {
                //parent keep track of the parent node of current node.
                parent = current;

                //If data is less than current's data, node will be inserted to the left of tree
                if (slope < current.slope || (Double.compare(slope, current.slope) == 0 &&  intercept < current.intercept)) {
                    current = current.left;
                    if(current == null) {
                        parent.left = newNode;
                        return false;
                    }
                }
                //If data is greater than current's data, node will be inserted to the right of treecp
                else if (slope > current.slope || (Double.compare(slope, current.slope) == 0 &&  intercept > current.intercept)) {
                    current = current.right;
                    if(current == null) {
                        parent.right = newNode;
                        return false;
                    }
                } else if (Double.compare(slope, current.slope) == 0 && Double.compare(intercept, current.intercept) == 0) {
                    System.out.println("YES collinear");
                    System.out.println("Slopes are " + slope + " " + current.slope);
                    System.out.println("Intercepts are " + intercept + " " + current.intercept);

                    return true;
                }
            }
        }
    }
}