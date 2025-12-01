package lab5;

public class MinHeap {
    int size;
    int loc;
    public int[] tree;
    public MinHeap(int l) {
        size = l+1;
        tree = new int[size];
        loc = 0;
    }
    public void add(int x) {
        if(loc == 0) {
            tree[1] = x;
            loc = 2;
        } else {
            tree[loc++] = x;
            up();
        }
    }
    public void up() {
        int pos = loc - 1;
        
    }
    public static void main(String[] args) {
        int[] a = {6,2,3,1,5,10,7,14};
        MinHeap theHeap = new MinHeap(a.length);
        for(int i = 0; i < a.length;i++) {

        }
    }
}
