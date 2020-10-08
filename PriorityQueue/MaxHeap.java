package PriorityQueue;

import java.util.ArrayList;



public class MaxHeap<T extends Comparable> implements PriorityQueueInterface<T> {
   public ArrayList<ArrayList<T>> heap ;
    public int size;
    public int queueitems;
   public int capacity;

    public MaxHeap() {
        this.size = 0;
        this.queueitems=0;
        this.capacity=10001;
        heap = new ArrayList<>(this.capacity);
        heap.add(0,new ArrayList<>());
        heap.get(this.size).add(null);
    }

    private int parent(int currpos) {
        return currpos / 2;
    }

    private int rightchild(int currpos) {
        return (2 * currpos) + 1;
    }

    private int leftchild(int currpos) {
        return (2 * currpos);
    }

    private void swap(int current, int parent) {
        ArrayList<T> temp;
        temp = heap.get(current);
        heap.set(current, heap.get(parent));
        heap.set(parent, temp);
    }

    @Override
    public void insert(T element) {
        if(this.size==0){
            this.size+=1;
            this.queueitems+=1;
            heap.add(this.size,new ArrayList<>());
            heap.get(this.size).add(element);
            return;
        }

        for(int i =1;i<=this.size;i++){
            ArrayList<T> queuelist = heap.get(i);
            if(queuelist!= null){
                if(queuelist.get(0).compareTo(element)==0){
                    queuelist.add(element);
                    this.queueitems+=1;
                    return;
                }
            }
        }
        this.size+=1;
        this.queueitems+=1;
        heap.add(this.size,new ArrayList<>());
        heap.get(this.size).add(element);
        int temp = this.size;
        if(parent(temp)>=1) {
            while (temp > 1 && heap.get(temp).get(0).compareTo(heap.get(parent(temp)).get(0)) > 0) {
                swap(temp, parent(temp));
                temp = parent(temp);
            }
        }
    }
    public void reinsert(T element){
        if(this.size==0){
            this.size+=1;
            this.queueitems+=1;
            heap.add(this.size,new ArrayList<>());
            heap.get(this.size).add(element);
            return;
        }

        for(int i =1;i<=this.size;i++){
            ArrayList<T> queuelist = heap.get(i);
            if(queuelist!= null){
                if(queuelist.get(0).compareTo(element)==0){
                    queuelist.add(0,element);
                    this.queueitems+=1;
                    return;
                }
            }
        }
        this.size+=1;
        this.queueitems+=1;
        heap.add(this.size,new ArrayList<>());
        heap.get(this.size).add(element);
        int temp = this.size;

        while(temp>1 && heap.get(temp).get(0).compareTo(heap.get(parent(temp)).get(0))>0){
            swap(temp,parent(temp));
            temp=parent(temp);

        }
    }

    @Override
    public T extractMax() {

        if(this.queueitems==0)
            return null;

        else if(this.heap.get(1).size()!=1){
            this.queueitems-=1;
            //System.out.println("ok");
            return heap.get(1).remove(0);
        }
        else{
            T popped = heap.get(1).remove(0);
            //System.out.println("not ok");
            heap.set(1,heap.get(this.size));
            this.size-=1;
            this.queueitems-=1;
            int i=1;
            while(leftchild(i)<=this.size){
                if(rightchild(i)<this.size){
                    if(heap.get(leftchild(i)).get(0).compareTo(heap.get(rightchild(i)).get(0))>0){
                        if(heap.get(leftchild(i)).get(0).compareTo(heap.get(i).get(0))>0){
                            swap(i,leftchild(i));
                            i=leftchild(i);
                        }
                        else{
                            break;
                        }
                    }
                    else{
                        if(heap.get(rightchild(i)).get(0).compareTo(heap.get(i).get(0))>0){
                            swap(i,rightchild(i));
                            i=rightchild(i);
                        }
                        else{
                            break;
                        }
                    }
                }
                else{
                    if(heap.get(leftchild(i)).get(0).compareTo(heap.get(i).get(0))>0){
                        swap(i,leftchild(i));
                        i=leftchild(i);
                    }
                    else
                        break;
                }
            }
            return popped;
        }
    }
}


