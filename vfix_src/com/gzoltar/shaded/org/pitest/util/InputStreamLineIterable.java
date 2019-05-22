package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.FunctionalIterable;
import com.gzoltar.shaded.org.pitest.functional.FunctionalList;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;

public class InputStreamLineIterable implements FunctionalIterable<String> {
   private final BufferedReader reader;
   private String next;

   public InputStreamLineIterable(Reader reader) {
      this.reader = new BufferedReader(reader);
      this.advance();
   }

   private void advance() {
      try {
         this.next = this.reader.readLine();
      } catch (IOException var2) {
         this.next = null;
      }

   }

   public String next() {
      String t = this.next;
      this.advance();
      return t;
   }

   public Iterator<String> iterator() {
      return new Iterator<String>() {
         public boolean hasNext() {
            return InputStreamLineIterable.this.next != null;
         }

         public String next() {
            return InputStreamLineIterable.this.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public FunctionalList<String> filter(F<String, Boolean> predicate) {
      return FCollection.filter(this, predicate);
   }

   public void forEach(SideEffect1<String> e) {
      FCollection.forEach(this, e);
   }

   public <B> FunctionalList<B> map(F<String, B> f) {
      return FCollection.map(this, f);
   }

   public <B> void mapTo(F<String, B> f, Collection<? super B> bs) {
      FCollection.mapTo(this, f, bs);
   }

   public <B> FunctionalList<B> flatMap(F<String, ? extends Iterable<B>> f) {
      return FCollection.flatMap(this, f);
   }

   public boolean contains(F<String, Boolean> predicate) {
      return FCollection.contains(this, predicate);
   }
}
