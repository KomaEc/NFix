package soot.util;

import java.util.Iterator;

public interface IterableNumberer<E> extends Numberer<E>, Iterable<E> {
   Iterator<E> iterator();
}
