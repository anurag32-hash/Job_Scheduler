package Trie;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Trie<T> implements TrieInterface<T> {
    private TrieNode<T> root;
    private TrieNode<T> trav;
    public Trie(){
        this.root= new TrieNode<T>('0');
    }

    public void printTrie(TrieNode trieNode) {
        if(trieNode.isword){
            Person person = (Person)trieNode.getValue();
            System.out.println("[Name: "+person.name+", "+"Phone="+person.phone_number+"]");
        }
        for(int i=0;i<128;i++){
            if(trieNode.children[i]!=null){
                printTrie(trieNode.children[i]);
            }
        }
    }

    @Override
    public boolean delete(String word) {
        if(search(word)==null){
          //  System.out.println("ERROR DELETING");
            return false;
        }
         else{
             trav=root;
             for(char i : word.toCharArray()){
                 TrieNode<T> child = trav.children[i];
                 if(child.count==1){
                     child.isword=false;
                     trav.children[i]=null;
                 //    System.out.println("DELETED");
                     return true;
                 }
                 else{
                     child.count-=1;
                     trav=child;
                 }
            }
            trav.isword=false;
            //System.out.println("DELETED");
            return true;
            }
    }

    @Override
    public TrieNode<T> search(String word) {
         trav = root;
        for (char i : word.toCharArray()){
            if(trav.children[i]!=null)
                trav=trav.children[i];
            else
                return null;
        }
        if(trav.isword){
            return trav;
        }
        else
            return null;
    }

    @Override
    public TrieNode<T> startsWith(String prefix) {
        trav = root;
        for(char i : prefix.toCharArray()){
            if(trav.children[i]!=null){
                trav=trav.children[i];
            }
            else
                return null;
        }
        return trav;
    }

    @Override
    public boolean insert(String word, T value) {

        trav = root;
        for (char i : word.toCharArray()) {
            if (trav.children[i] == null) {
                trav.children[i] = new TrieNode<T>(i);
                trav = trav.children[i];
            }
            else {
                trav = trav.children[i];
            }
            trav.count += 1;
        }
        if(!trav.isword){
            trav.isword = true;
            trav.value = value;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void printLevel(int level) {
        int lev=1;
        String str="";
        if(this.root==null)
            return;
        Queue<TrieNode<T>> q1 = new LinkedList<>();
        trav=this.root;
        for(int i=0; i<128;i++){
            if(trav.children[i]!=null){
                q1.offer(trav.children[i]);
            }
        }
        q1.offer(null);
        while (!q1.isEmpty()) {
            if (lev < level) {
                trav = q1.poll();
                if (trav != null) {
                    for (int i = 0; i < 128; i++) {
                        if (trav.children[i] != null) {
                            q1.offer(trav.children[i]);
                        }
                    }
                }
                else {
                    if (!q1.isEmpty()) {
                        lev += 1;
                        if(lev==level){
                            System.out.print("Level " + level + ": ");
                            q1.offer(null);
                        }
                        else
                            q1.offer(null);
                    }
                }
            }
            else if (lev == level) {
                trav = q1.poll();
                if (trav != null) {
                    str = str.concat(trav.alpha + "");
                    for (int i = 0; i < 128; i++) {
                        if (trav.children[i] != null) {
                            q1.offer(trav.children[i]);
                        }
                    }
                }
                else {
                    if (!q1.isEmpty()) {
                        char[] arr = str.toCharArray();
                        Arrays.sort(arr);
                        String sorted = String.valueOf(arr);
                        sorted = sorted.trim();
                        for(int i=0;i<sorted.length()-1;i++){
                            System.out.print(sorted.charAt(i)+",");
                        }
                        System.out.println(sorted.charAt(sorted.length()-1));
                        System.out.println("-------------");
                        q1.offer(null);
                        return;
                    }
                    else{
                        char[] arr = str.toCharArray();
                        Arrays.sort(arr);
                        String sorted = String.valueOf(arr);
                        sorted = sorted.trim();
                        for(int i=0;i<sorted.length()-1;i++){
                            System.out.print(sorted.charAt(i)+",");
                        }
                        System.out.println(sorted.charAt(sorted.length()-1));
                        System.out.println("-------------");
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void print() {
        int level=1;
        String str="";
        if(this.root==null)
            return;

        Queue<TrieNode<T>> q = new LinkedList<>();
        trav=this.root;
        for(int i=0; i<128;i++){
            if(trav.children[i]!=null){
                q.offer(trav.children[i]);
            }
        }
        q.offer(null);
        System.out.println("Printing Trie");
        System.out.print("Level "+level+": ");
        while(!q.isEmpty()){
            trav  = q.poll();
            if(trav!=null){
                str = str.concat(trav.alpha + "");
                for (int i = 0; i < 128; i++) {
                    if (trav.children[i] != null) {
                        q.offer(trav.children[i]);
                    }
                }
            }
            else{
                 if(!q.isEmpty()){
                     char[] arr = str.toCharArray();
                     Arrays.sort(arr);
                     String sorted = String.valueOf(arr);
                     sorted = sorted.trim();
                     for(int i=0;i<sorted.length()-1;i++){
                         System.out.print(sorted.charAt(i)+",");
                     }
                     System.out.println(sorted.charAt(sorted.length()-1));
                     str="";
                     level+=1;
                     System.out.print("Level "+level+": ");
                     q.offer(null);
                 }
                 else {
                     char[] arr = str.toCharArray();
                     Arrays.sort(arr);
                     String sorted = String.valueOf(arr);
                     sorted = sorted.trim();
                     for(int i=0;i<sorted.length()-1;i++){
                         System.out.print(sorted.charAt(i)+",");
                     }
                     System.out.println(sorted.charAt(sorted.length()-1));
                     str="";
                     level+=1;
                     System.out.println("Level "+level+": ");
                     System.out.println("-------------");
                 }
            }
        }

    }

}