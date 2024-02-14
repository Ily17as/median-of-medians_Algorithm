//Galiev Ilyas
//from lab 4 materials

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //reading the input from compiler
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();

        //Creating map
        Map<Integer, String> map = new HashMap<>(n);

        //Reading the students from compiler
        // and filling the map with keys
        for (int i = 0; i < n; i++){
            String line = sc.nextLine();
            String[] words = line.split(" ");
            String number = words[0];
            String name = words[1] + " " + words[2];
            map.put(Integer.parseInt(number), name);
        }

        //Creating set of entries
        List<KeyValuePair<Integer, String>> entries = map.entrySet();

        //Applying median of medians for entities
        KeyValuePair<Integer, String> student = select(entries, (n - 1) / 2);


        //Printing the middle element
        System.out.println(student.value);

    }
    public static KeyValuePair<Integer, String> select(List<KeyValuePair<Integer, String>> list, int k) {
        //returning the value when array would be small enough
        if (list.size() <= 10) {
            sort(list);
            return list.get(k);
        }

        //Finding medians
        ArrayList<KeyValuePair<Integer, String>> medians = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 5) {
            ArrayList<KeyValuePair<Integer, String>> sublist = new ArrayList<>(subList(list, i, min(i + 5, list.size())));
            sort(sublist);
            medians.add(sublist.get(sublist.size() / 2));
        }

        //Finding pivot
        KeyValuePair<Integer, String> pivot = select(medians, medians.size() / 2);

        //Dividing the array for >pivot, <pivot, =pivot
        ArrayList<KeyValuePair<Integer, String>> lower = new ArrayList<>(), higher = new ArrayList<>(), equal = new ArrayList<>();
        for (KeyValuePair<Integer, String> num : list) {
            if (num.key < pivot.key) {
                lower.add(num);
            } else if (num.key > pivot.key) {
                higher.add(num);
            } else {
                equal.add(num);
            }
        }

        //Finding k-th element in this arrays
        if (k < lower.size()) {
            return select(lower, k);
        } else if (k >= lower.size() + equal.size()) {
            return select(higher, k - lower.size() - equal.size());
        } else {
            return pivot;
        }
    }

    //Selection sort for median of medians algorithm
    public static void sort(List<KeyValuePair<Integer, String>> list){
        for (int i = 0; i < list.size() - 1; i++){
            KeyValuePair<Integer,String> mn = list.get(i);
            int temp = i;
            for (int j = i + 1; j < list.size(); j++){
                if (mn.key > list.get(j).key){
                    mn = list.get(j);
                    temp = j;
                }
            }
            for (int x = temp; x > i; x--){
                list.set(x, list.get(x - 1));
            }
            list.set(i, mn);
        }
    }

    //Function that returns the minimum number out of 2
    public static int min(int a, int b){
        if (a < b){
            return a;
        } else {
            return b;
        }
    }

    //Function returns the sublist from star(inclusively) till the end(explicitly).
    public static List<KeyValuePair<Integer, String>> subList(List<KeyValuePair<Integer, String>> arr, int start, int end){
        List<KeyValuePair<Integer, String>> sublist = new ArrayList<>();
        for (int i = start; i < end; i++){
            sublist.add(arr.get(i));
        }
        return sublist;
    }
}

//Interface for map class
interface Map<K, V> {
    int size();

    boolean isEmpty();

    V get(K key);

    void put(K key, V value);

    void remove(K key);

    List<KeyValuePair<K, V>> entrySet();
}

//Class for objects with entry type for mep and set in main.
//Object of this type have key and value.
class KeyValuePair<K, V> {
    K key;
    V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

}

//Hashmap data type realisation
class HashMap<K, V> implements Map<K, V> {
    int mapSize;
    int capacity;
    List<KeyValuePair<K, V>>[] hashTable;

    public HashMap(int capacity) {
        this.capacity = capacity;
        this.mapSize = 0;
        this.hashTable = new List[capacity];
        for (int i = 0; i < this.capacity; i++) {
            this.hashTable[i] = new LinkedList<>();
        }
    }

    public KeyValuePair<K, V> getEntry(K key) {
        int hashcode = Math.abs(key.hashCode()) % capacity;
        for (KeyValuePair<K, V> entry : hashTable[hashcode]) {
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return mapSize;
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public V get(K key) {
        int hashcode = Math.abs(key.hashCode()) % capacity;
        for (KeyValuePair<K, V> entry : hashTable[hashcode]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        try {
            if (get(key) != null) {
                KeyValuePair<K, V> temp = getEntry(key);
                temp.value = value;
            } else {
                int hash = Math.abs(key.hashCode()) % this.capacity;
                hashTable[hash].add(new KeyValuePair<>(key, value));
                this.mapSize++;
                if (mapSize > capacity) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            System.out.println("Hashtable is full");
            System.exit(1);
        }
    }

    @Override
    public void remove(K key) {
        int hashcode = Math.abs(key.hashCode()) % capacity;
        for (KeyValuePair<K, V> entry : hashTable[hashcode]) {
            if (entry.key.equals(key)) {
                hashTable[hashcode].remove(entry);
            }
        }
    }

    @Override
    public List<KeyValuePair<K, V>> entrySet() {
        List<KeyValuePair<K, V>> entries = new ArrayList<>();
        for (List<KeyValuePair<K, V>> list : this.hashTable) {
            for (KeyValuePair<K, V> entry : list) {
                entries.add(entry);
            }
        }
        return entries;
    }
}