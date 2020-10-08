package Trie;

public class Person {
    String name;
    String phone_number;

    public Person(String name, String phone_number) {
        this.name=name;
        this.phone_number=phone_number;
    }

    public String getName() {
        return this.name;
    }
    public String getPhone_number(){
        return this.phone_number;
    }
    public String toString(){
        String str = "[Name: "+this.getName()+", "+"Phone="+this.getPhone_number()+"]";
        return str;
    }

}
