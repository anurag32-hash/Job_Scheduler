package RedBlack;

import Util.RBNodeInterface;

import java.util.ArrayList;
import java.util.List;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {
    public T key;
    public List<E> value = new ArrayList<E>();
    public RedBlackNode<T,E> left,right,parent;
    String color;

    RedBlackNode (T key,E val){
        if(key==null && val==null)
            this.value=null;
        else {
            this.key = key;
            this.value.add(val);
            this.left = null;
            this.right = null;
            this.parent = null;
            this.color = "red";
        }
    }
    RedBlackNode(T key){
        this.key=key;
        this.left=null;
        this.right=null;
        this.parent=null;
        this.color="red";
        this.value=null;
    }


    @Override
    public E getValue() {
        return this.value.get(0);
    }

    @Override
    public List<E> getValues() {
        return this.value;
    }

    public int compareTo(RedBlackNode<T,E> rb1){
        return this.key.compareTo(rb1.key);
    }
    public String toString (){
        return (String)this.key;
    }
}
