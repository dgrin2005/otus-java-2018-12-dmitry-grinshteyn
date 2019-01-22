package ru.otus;

import java.util.*;

public class MyArrayList<T> implements List<T> {

    T[] array = (T[]) new Object[]{};

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        return Arrays.asList(array).contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return Arrays.asList(array).iterator();
    }

    @Override
    public Object[] toArray() {
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        try {
            array  = Arrays.copyOf(array, array.length + 1);
            array[array.length - 1] = t;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        try {
            int i = 0;
            boolean found = false;
            while (i < array.length && !found) {
                found = array[i] == o;
                i++;
            }
            if (found) {
                i--;
                System.arraycopy(array, i + 1, array, i, array.length - 1 - i);
                array  = Arrays.copyOf(array, array.length - 1);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean containsAll = true;
        Iterator<?> iterator = c.iterator();
        List list = Arrays.asList(array);
        while (iterator.hasNext()) {
            containsAll &= list.contains(iterator.next());
        }
        return containsAll;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        try {
            for (Object aC : c) {
                array = Arrays.copyOf(array, array.length + 1);
                array[array.length - 1] = (T) aC;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        try {
            T[] arrayNew = Arrays.copyOf(array, index);
            for (Object aC : c) {
                arrayNew = Arrays.copyOf(arrayNew, arrayNew.length + 1);
                arrayNew[arrayNew.length - 1] = (T) aC;
            }
            for(int i = index; i < array.length; i++) {
                arrayNew = Arrays.copyOf(arrayNew, arrayNew.length + 1);
                arrayNew[arrayNew.length - 1] = array[i];
            }
            array = arrayNew;
            return true;
        } catch (Exception ex) {
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
            T[] array1 = (T[]) new Object[]{};
            for (T anArray : array) {
                if (c.contains(anArray)) {
                    array1 = Arrays.copyOf(array1, array1.length + 1);
                    array1[array1.length - 1] = anArray;
                }
            }
            array = array1;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void clear() {
        array  = Arrays.copyOf(array, 0);
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        try {
            array[index] = element;
            return element;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void add(int index, T element) {
        try {
            array  = Arrays.copyOf(array, array.length + 1);
            System.arraycopy(array, index, array, index + 1, array.length - 1 - index);
            array[index] = element;
        } catch (Exception ignored) {
        }
    }

    @Override
    public T remove(int index) {
        try {
            T element = array[index];
            System.arraycopy(array, index + 1, array, index, array.length - 1 - index);
            array  = Arrays.copyOf(array, array.length - 1);
            return element;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public int indexOf(Object o) {
        try {
            int i = 0;
            boolean found = false;
            while (i < array.length && !found) {
                found = array[i] == o;
                i++;
            }
            if (found) {
                return --i;
            } else {
                return -1;
            }
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        try {
            int i = array.length - 1;
            boolean found = false;
            while (i >= 0 && !found) {
                found = array[i] == o;
                i--;
            }
            if (found) {
                return ++i;
            } else {
                return -1;
            }
        } catch (Exception ex) {
            return -1;
        }
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
        MyArrayList<T> myArrayList = new MyArrayList<>();
        myArrayList.addAll(Arrays.asList(array).subList(fromIndex, toIndex + 1));
        return myArrayList;
    }

    public boolean addAll(Collection<? super T> c, T... elements) {
        try {
            for (Object aC : c) {
                array = Arrays.copyOf(array, array.length + 1);
                array[array.length - 1] = (T) aC;
            }
            for (T i: elements)  {
                add(i);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    static <T> void copy(List<? super T> dest, List<? extends T> src) {
        dest.clear();
        dest.addAll(src);
    }

    static <T> void sort(List<T> list, Comparator<? super T> c) {
        for (int m = 0; m < list.size(); m++) {
            for (int i = m; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    if (c.compare(list.get(i), list.get(j)) > 0) {
                        T a = list.get(i);
                        T b = list.get(j);
                        list.remove(i);
                        list.add(i, b);
                        list.remove(j);
                        list.add(j, a);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "MyArrayList = " +
                Arrays.toString(array);
    }
}
