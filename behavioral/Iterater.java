/*
    This is very important design pattern.
    Infact so important that you wont ever get to implement it as it is already implemented for every data type.
    This one is simple, it allows serial iteration over your datatype or object.
    From interview point of view, its too niche and wont be asked most of the times.
 */

import java.util.Iterator;
import java.util.Stack;

public class Iterater {
    //Its not spelling mistake, its conflicting with interface with same name
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree(4);
        bst.AddNode(3);
        bst.AddNode(5);
        bst.AddNode(8);
        bst.AddNode(2);
        for (Integer val: bst) {
            System.out.println("Node:" + val);
        }
    }
}

class BinarySearchTree implements Iterable<Integer> {
    public Integer value;
    public BinarySearchTree childR;
    public BinarySearchTree childL;
    public BinarySearchTree parent;

    public BinarySearchTree(Integer value) {
        this.value = value;
    }

    public void AddNode(Integer value) {
        if (this.value > value) {
            if (this.childL != null) {
                this.childL.AddNode(value);
            } else {
                this.childL = new BinarySearchTree(value);
            }
        } else {
            if (this.childR != null) {
                this.childR.AddNode(value);
            } else {
                this.childR = new BinarySearchTree(value);
            }
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new InOrderIterator(this);
    }

    private class InOrderIterator implements Iterator<Integer> {
        private final Stack<BinarySearchTree> stack = new Stack<>();

        public InOrderIterator(BinarySearchTree root) {
            pushLeft(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Integer next() {
            BinarySearchTree node = stack.pop();
            Integer val = node.value;
            if (node.childR != null) {
                pushLeft(node.childR);
            }
            return val;
        }

        private void pushLeft(BinarySearchTree node) {
            while (node != null) {
                stack.push(node);
                node = node.childL;
            }
        }
    }
}