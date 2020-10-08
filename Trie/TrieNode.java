package Trie;
import Util.NodeInterface;

public class TrieNode<T> implements NodeInterface<T> {
    public TrieNode<T>[] children = new TrieNode[128];
    public boolean isword;
   public T value;
    public int count ;
    public char alpha;
    TrieNode(char alpha){
        this.value=null;
        this.isword=false;
        this.count=0;
        this.alpha=alpha;
        for(int i=0; i<128;i++){
            this.children[i]=null;
        }
    }


    @Override
    public T getValue() {
        return this.value;
    }



}