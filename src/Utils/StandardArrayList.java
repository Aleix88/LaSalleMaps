package Utils;

import Modelv2.CityInfo;

import java.util.*;

public class StandardArrayList<E> implements List<E>{


    private final static int START_SIZE = 10;

    private Object[] data;
    private int numObjects = 0;

    private int i;
    private Object[] dataAux;

    public StandardArrayList(){
        data = new Object[START_SIZE];
    }

    @Override
    public boolean add(Object o){
        if(numObjects + 1 == data.length){
            dataAux = data;
            data = new Object[(int)(1.5*numObjects)];
            for(i = 0; i < numObjects; i++){
                data[i] = dataAux[i];
            }
        }
        data[numObjects++] = o;

        return false;
    }

    E data(int index) {
        return (E) data[index];
    }

    @Override
    public E get(int index){
        if(index < numObjects){
            return data(index);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public int size() {
        return numObjects;
    }
    @Override
    public void clear() {
        numObjects = 0;
    }

    @Override
    public boolean isEmpty() {
        return numObjects == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        if(index >= data.length){
            dataAux = data;
            data = new Object[(index+1)];
            for(i = 0; i < numObjects; i++){
                data[i] = dataAux[i];
            }
            numObjects = index+1; //Considerem que te objectes fins l'index escollit
        }
        data[index] = element;
        return element;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }




}
