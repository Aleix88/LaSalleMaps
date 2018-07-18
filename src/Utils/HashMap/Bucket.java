package Utils.HashMap;


import Utils.StandardArrayList;

public class Bucket<K,V> {
    /*Aquesta implementació també es podria fer amb una LinkedList, de fet, potser seria recomanable. No s'ha fet així per dues raons:
    * 1. No haver d'implementar una LinkedList
    * 2. S'ha mencionat que el cost de insertar es absolutament irrelevant. Aixó tampoc seria justificació suficient, ja que si poguessis millorar
    * el cost de insersió sense empitjorar res mès, lo ideal seria implementar una linked list, pero no tot tan simple:
    * 3. Una linkedlist consumeix moltissim mès memoria. Podriem dir que la memoria no ens importa en absolut, pero no podem dir aixó tenint en compte que hi ha 2
    * millions de ciutats al mon, que probablement segons la aplicació s'expandeixi es requereixin mès detalls i informacio, etc..
    * https://stackoverflow.com/questions/11564352/arraylist-vs-linkedlist-from-memory-allocation-perspective  <-- Super ben explicat
    * 4. La que es trobo que es mès important: https://stackoverflow.com/questions/38785292/why-is-iterating-through-linkedlist-slow
    * La cerca en un linkedlist , tot i ser O(n), es notablement mès lenta que la cerca per un arraylist ! (Coses de la CPU, i el fet que mentre un arraylist
    * te posicions contigues(no esta clar en la JVM, pero es menciona que si en la majoria de casos)
     * un linkedlist està espargit per la memoria. CPU pot predir el primer cas, no el segon*/
    private StandardArrayList<HashNode<K,V>> bucketlist;

    public Bucket() {
        bucketlist = new StandardArrayList<>();
    }

    public StandardArrayList<HashNode<K, V>> getBucketlist() {
        return bucketlist;
    }

    public void setBucketlist(StandardArrayList<HashNode<K, V>> bucketlist) {
        this.bucketlist = bucketlist;
    }
}
