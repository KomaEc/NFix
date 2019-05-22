package org.jboss.util.collection;

import java.util.Map.Entry;

public interface ValueRef<K, V> extends Entry<K, V> {
   V get();
}
