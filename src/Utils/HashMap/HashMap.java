package Utils.HashMap;


import Utils.StandardArrayList;


public class HashMap<K, V>{

    private StandardArrayList<Bucket<K,V>> map;
    private int numNodes; //Num de nodes que s'afegeixen, pel loadfactor.
    private static final double LOAD_FACTOR = 0.6;
    private static final int BUCKETS = 20;

    public HashMap() {
        map = new StandardArrayList<>();
        this.numNodes = 0;
        for(int i = 0; i < BUCKETS; i++){
            map.add(new Bucket<>()); //No els hi estic posant info, simplement els inicialitzo.
        }
    }

    public HashMap(int size) {
        map = new StandardArrayList<>();
        this.numNodes = 0;
        for(int i = 0; i < size; i++){
            map.add(new Bucket<>()); //No els hi estic posant info, simplement els inicialitzo.
        }
    }

    // No miro si els valors son repetits. Bàsicament perque de tota la vida sempre es busca abans d'afegir. I així permetem repeticions si algu vol..
    public void put(K key, V value) {
        putAux(key,value);
        numNodes++;
        /*Important, el load factor: https://stackoverflow.com/questions/10901752/what-is-the-significance-of-load-factor-in-hashmap
        * Aquest es bàsicament numNodes/sizeArray. Es parla que com mès alt es el valor menys espai gastes pero tardes mès a trobar el node correcte, i com mès
        * baix a l'inrevés. A nosaltres per tant ens interessa un load factor baix. Per defecte Java (Els experts que van implementar HashMap), el van deixar a 0.75
        * , perque d'ona un bon equilibri.
        * Tenint en compte que volem buscar el mès rapid posible, ens la juguem amb un load factor de 0.6 de moment. Potser passa que es reconstrueix tot mès sovint,
        * pero hem parlat que el cost de inserir no importa*/
        if (numNodes/map.size() >= LOAD_FACTOR) {
            StandardArrayList<Bucket<K, V>> temp = map;
            map = new StandardArrayList<>();
            int newsize = temp.size()*2;
            for (int i = 0; i < newsize; i++) {
                map.add(new Bucket<>());
            }
            for(int i = 0; i < temp.size(); i++){
                Bucket<K,V> b = temp.get(i);
                for(int j = 0; j < b.getBucketlist().size(); j++) {
                    HashNode<K,V> hnAux = b.getBucketlist().get(j);
                    putAux(hnAux.getKey(), hnAux.getValue());
                }
            }
        }
    }
    private void putAux(K key, V value){
        StandardArrayList<HashNode<K,V>> bucketarray = map.get(getIndexAux(key)).getBucketlist();
        HashNode<K,V> hn = new HashNode<>(key, value);
        bucketarray.add(hn);
    }

    public V get(K key) {
        StandardArrayList<HashNode<K,V>> bucketarray = map.get(getIndexAux(key)).getBucketlist();
        //foreach + rapid que standard for. Excepte que no el puc utilitzar perque no implemento ni iterator, ni hasNext, ni next :)
        for(int i = 0; i < bucketarray.size(); i++){
            if(bucketarray.get(i).getKey().equals(key)){  //String fa override del .equals, vigilar amb altres objectes
               return  bucketarray.get(i).getValue();
            }
        }
        return null; //Si no troba la clau. En aquest cas es busca a la API
    }

    private int getIndexAux(K key) {
        /*Hem de vigilar. Jo estic intentant fer el HashMap lo mès general possible, i aixó implica fer concessions. utilitzar key.hashcode() amb un tipus
         generic significa que depenen del objecte i de si han fet @Override de hashcode()
         Si no hi ha override: https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--
         Si hi ha override: Depen de la classe
         En qualsevol cas, si no t'agrada, fes override dels mètodes
         En el nostre cas, que sabem que utilitzarem strings, on el .hashcode() ha estat overrided:
         La funció de hashcode es la seguent:  s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
         La utilitzarem basicament per dos raons:
         1. Ha estat feta per professionals que saben el que feien
         2. Es mencionen que te molts bons resultats  --> https://stackoverflow.com/questions/9406775/why-does-string-hashcode-in-java-have-many-conflicts*/
        int hashCode = key.hashCode();
        int index = hashCode % map.size();
        return Math.abs(index); //Amb strings molt llargs el hash podria donar negatiu(supera max valor int: Integer.MAX_VALUE +1)
                                // . Amb altres objectes depen, com hem mencionat
    }
    public void clear(){ //Reinicialitzo amb mida default
        this.numNodes = 0;
        map.clear();
        for(int i = 0; i < BUCKETS; i++){
            map.add(new Bucket<>()); //No els hi estic posant info, simplement els inicialitzo.
        }
    }
}