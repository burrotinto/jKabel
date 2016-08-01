/*
 * jKabel - Ein hochperfomantes, extremstanpassungsfähiges Mehrbenutzersystem zur erfassung von Kabelstrecken
 *
 * Copyright (C) 2016 Florian Klinger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.burrotinto.usefull.list;

import java.util.*;

/**
 * Set das abhängig wie oft ein Object hinengelegt wird danach sortiert wird.
 * Created by Florian Klinger on 01.08.16.
 */
public class SortedSetAnzahlDerEingefuegtenElemente<T> implements Set<T> {

    private final HashMap<T, Integer> map = new HashMap<T, Integer>();

    public static void main(String[] args) {
        SortedSetAnzahlDerEingefuegtenElemente<String> test = new SortedSetAnzahlDerEingefuegtenElemente<>();
        test.add("Xa");
        test.add("Auto");
        test.add("Xa");
        test.add("Auto");
        test.add("Auto");
        for (String s : test
            ) {
            System.out.println(s);
        }
    }

    private synchronized List<T> getSort() {
        List<T> list = new ArrayList<T>(map.keySet());
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T t, T t1) {
                return map.get(t1) - map.get(t);
            }
        });
        return list;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return map.get(o) != null;
    }

    @Override
    public Iterator iterator() {
        return getSort().iterator();
    }

    @Override
    public Object[] toArray() {
        return getSort().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return null;
    }

    @Override
    public synchronized boolean add(T o) {
        if (map.get(o) == null) {
            map.put(o, 0);
        } else {
            map.put(o, map.get(o) + 1);
        }
        return true;
    }

    @Override
    public synchronized boolean remove(Object o) {
        if (map.containsKey(o)) {
            map.remove(o);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return map.keySet().containsAll(collection);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> collection) {
        for (T t : collection) {
            add(t);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> collection) {
        boolean remove = false;
        for (Object t : collection) {
            if (remove(t)) remove = true;
        }
        return remove;
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }
}
