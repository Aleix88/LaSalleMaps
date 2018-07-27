package Utils.OrderedArrayList;


public class StringOrderedArrayList<E> {

    private final static int START_SIZE = 10;

    private ObjectStringOrderedArrayList[] data;
    private int numObjects = 0;

    private int i;
    private int j;
    private int k;
    private ObjectStringOrderedArrayList[] dataAux;

    public StringOrderedArrayList(){
        data = new ObjectStringOrderedArrayList[START_SIZE];

    }

    public void addOrdered(ObjectStringOrderedArrayList o){
        i = 0;
        while(i < numObjects && data[i].getOrder().compareTo(o.getOrder()) <= 0) i++;
        numObjects++;
        if(numObjects == data.length){
             dataAux = data;
             data = new ObjectStringOrderedArrayList[(int)(1.5*numObjects)];
             for(j = 0; j < i; j++){
                 data[j] = dataAux[j];
             }
             data[i++] = o;
             for(;j < numObjects;j++){
                 data[i++] = dataAux[j];
             }
        }else{
            for(j = numObjects-1; j > i; j--){
                data[j] = data[j-1]; //VIGILAR AIXO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
            data[i] = o;
        }

    }

    E data(int index) {
        return (E)data[index].getObject();
    }

    public E get(int index){
        if(index < numObjects){
            return data(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void clear(){
        numObjects = 0;
    }
    /**
     * O(log n) ==> BINARY SEARCH
     * @param s String que indica l'ordre del objecte a trobar. Si l'ordre es el mateix, es l'objecte.
     * @return CityInfo trobat (null sinó existeix)
     */
    public E binarySearch(String s){
        i = 0;
        j = numObjects - 1;
        while(i <= j){
            k = i + (j-i)/2;
            if(data[k].getOrder().equals(s)) return data(k);
            if(data[k].getOrder().compareTo(s) < 0){
                i = k + 1; //Eliminem esquerra
            }else{
                j = k - 1; //Eliminem dreta
            }
        }
        return null;
    }
   /* public int binarySearchIndex(String s){
        i = 0;
        j = numObjects;
        while(i <= j){
            k = i + (j-i)/2;
            if(data[k].getOrder().equals(s)) return k;
            if(data[k].getOrder().compareTo(s) < 0){
                i = k + 1; //Eliminem esquerra
            }else{
                j = k - 1; //Eliminem dreta
            }
        }
        return -1;
    }
*/
    public E linealSearch(String s){
      for(i = 0; i < numObjects; i++){
          if(data[i].getOrder().equals(s)) return data(i);
      }
        return null;
    }


    public int length(){
        return numObjects;
    }
}
