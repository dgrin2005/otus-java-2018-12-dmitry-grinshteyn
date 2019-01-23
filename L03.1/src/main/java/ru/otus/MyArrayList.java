package ru.otus;

import java.util.*;

public class MyArrayList<T> implements List<T> {

    private static final int CHANGE_SIZE   = 10;
    private int size = 0;
    private T[] array = (T[]) new Object[size];

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>) Arrays.asList(toArray()).iterator();
    }

    @Override
    public Object[] toArray() {
        T[] array1 = (T[]) new Object[size];
        System.arraycopy(array, 0, array1, 0, size);
        return array1;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    private void increaseArraySize() {
        if (array.length < size + 1) {
            array = Arrays.copyOf(array, array.length + CHANGE_SIZE);
        }
    }
    
    private void decreaseArraySize() {
        if (array.length - CHANGE_SIZE >= size) {
            array = Arrays.copyOf(array, array.length - CHANGE_SIZE);
        }
    }
    
    @Override
    public boolean add(T t) {
        try {
            increaseArraySize();
            size++;
            array[size - 1] = t;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i >= 0) {
            System.arraycopy(array, i + 1, array, i, size - 1 - i);
            size--;
            decreaseArraySize();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean containsAll = true;
        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext() && containsAll) {
            containsAll = indexOf(iterator.next()) >= 0;
        }
        return containsAll;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        try {
            T[] array1 = (T[]) new Object[size + c.size()];
            System.arraycopy(array, 0, array1, 0, size);
            int i = 0;
            for (Object aC : c) {
                array1[size + i++] = (T) aC;
            }
            array = array1;
            size += c.size();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index <= size && index >= 0) {
            T[] array1 = (T[]) new Object[size + c.size()];
            System.arraycopy(array, 0, array1, 0, index);
            int i = 0;
            for (Object aC : c) {
                array1[index + i++] = (T) aC;
            }
            for (i = index; i < size; i++) {
                array1[c.size() + i] = array[i];
            }
            array = array1;
            size += c.size();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removeAll = true;
        for (Object aC : c) {
            removeAll &= remove(aC);
        }
        return removeAll;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        try {
            T[] array1 = (T[]) new Object[size];
            int i = 0;
            for (int j = 0; j < size; j++) {
                if (c.contains(array[j])) {
                    array1[i++] = array[j];
                }
            }
            array = array1;
            size = i;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void clear() {
        size = 0;
        array = (T[]) new Object[size];
    }

    @Override
    public T get(int index) {
        if (index < size && index >= 0) {
            return array[index];
        } else {
            return null;
        }
    }

    @Override
    public T set(int index, T element) {
        if (index < size && index >= 0) {
            try {
                array[index] = element;
                return element;
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void add(int index, T element) {
        if (index <= size && index >= 0) {
            increaseArraySize();
            System.arraycopy(array, index, array, index + 1, size - index);
            size++;
            array[index] = element;
        }
    }

    @Override
    public T remove(int index) {
        if (index < size && index >= 0) {
            T element = get(index);
            System.arraycopy(array, index + 1, array, index, size - 1 - index);
            size--;
            return element;
        } else {
            return null;
        }
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        while (i < size) {
            if (array[i] == o) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = size - 1;
        while (i >= 0) {
            if (array[i] == o) {
                return i;
            }
            i--;
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex >= 0 && toIndex >= fromIndex && toIndex < size) {
            T[] array1 = (T[]) new Object[toIndex - fromIndex + 1];
            System.arraycopy(array, fromIndex, array1, 0, toIndex + 1 - fromIndex);
            MyArrayList<T> myArrayList = new MyArrayList<>();
            myArrayList.setArray(array1);
            myArrayList.setSize(toIndex - fromIndex + 1);
            return myArrayList;
        } else {
            return null;
        }
    }

    public boolean addAll(Collection<? super T> c, T... elements) {
        try {
            T[] array1 = (T[]) new Object[size + c.size() + elements.length];
            System.arraycopy(array, 0, array1, 0, size);
            int i = 0;
            for (Object aC : c) {
                array1[size + i++] = (T) aC;
            }
            for (T el: elements)  {
                array1[size + i++] = el;
            }
            array = array1;
            size += c.size() + elements.length;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    static <T> void copy(List<? super T> dest, List<? extends T> src) {
        dest.clear();
        dest.addAll(src);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Object[] a = toArray();
        Arrays.sort(a, (Comparator) c);
        array = (T[]) a;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setArray(T[] array) {
        this.array = array;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("MyArrayList = [");
        for (int i = 0; i < size; i++) {
            string.append(array[i] + ", ");
        }
        if (size > 0) {
            string.delete(string.length() - 2, string.length());
        }
        string.append("]");
        string.append("; size = " + size);
        string.append("; array length = " + array.length);
        return String.valueOf(string);
    }


}
