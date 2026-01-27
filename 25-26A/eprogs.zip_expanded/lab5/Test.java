package lab5;

public class Test {
    Node root;
    public void add(int key) {
        Node newNode = new Node(key);
        if(root == null) {
            root = newNode;
        } else {
            Node current = root;
            while (true) {
                Node parent = current;
                if(key < current.key) {
                    current = current.leftChild;
                    if(current == null) {
                        parent.leftChild = newNode;
                        return;
                    }
                } else {
                    current = current.rightChild;
                    if(current == null) {
                        parent.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        Test tr = new Test();
        tr.add(20);
        tr.add(10);
    }
}

class Node {
    int key;
    Node leftChild;
    Node rightChild;
    public Node(int key) {
        this.key = key;
    }
}
