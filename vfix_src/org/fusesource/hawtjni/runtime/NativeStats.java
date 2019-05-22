package org.fusesource.hawtjni.runtime;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class NativeStats {
   private final HashMap<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> snapshot;

   public NativeStats(NativeStats.StatsInterface... classes) {
      this((Collection)Arrays.asList(classes));
   }

   public NativeStats(Collection<NativeStats.StatsInterface> classes) {
      this(snapshot(classes));
   }

   private NativeStats(HashMap<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> snapshot) {
      this.snapshot = snapshot;
   }

   public void reset() {
      Iterator i$ = this.snapshot.values().iterator();

      while(i$.hasNext()) {
         ArrayList<NativeStats.NativeFunction> functions = (ArrayList)i$.next();
         Iterator i$ = functions.iterator();

         while(i$.hasNext()) {
            NativeStats.NativeFunction function = (NativeStats.NativeFunction)i$.next();
            function.reset();
         }
      }

   }

   public void update() {
      Iterator i$ = this.snapshot.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> entry = (Entry)i$.next();
         NativeStats.StatsInterface si = (NativeStats.StatsInterface)entry.getKey();
         Iterator i$ = ((ArrayList)entry.getValue()).iterator();

         while(i$.hasNext()) {
            NativeStats.NativeFunction function = (NativeStats.NativeFunction)i$.next();
            function.setCounter(si.functionCounter(function.getOrdinal()));
         }
      }

   }

   public NativeStats snapshot() {
      NativeStats copy = this.copy();
      copy.update();
      return copy;
   }

   public NativeStats copy() {
      HashMap<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> rc = new HashMap(this.snapshot.size() * 2);
      Iterator i$ = this.snapshot.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> entry = (Entry)i$.next();
         ArrayList<NativeStats.NativeFunction> list = new ArrayList(((ArrayList)entry.getValue()).size());
         Iterator i$ = ((ArrayList)entry.getValue()).iterator();

         while(i$.hasNext()) {
            NativeStats.NativeFunction function = (NativeStats.NativeFunction)i$.next();
            list.add(function.copy());
         }

         rc.put(entry.getKey(), list);
      }

      return new NativeStats(rc);
   }

   public NativeStats diff() {
      HashMap<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> rc = new HashMap(this.snapshot.size() * 2);
      Iterator i$ = this.snapshot.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> entry = (Entry)i$.next();
         NativeStats.StatsInterface si = (NativeStats.StatsInterface)entry.getKey();
         ArrayList<NativeStats.NativeFunction> list = new ArrayList(((ArrayList)entry.getValue()).size());
         Iterator i$ = ((ArrayList)entry.getValue()).iterator();

         while(i$.hasNext()) {
            NativeStats.NativeFunction original = (NativeStats.NativeFunction)i$.next();
            NativeStats.NativeFunction copy = original.copy();
            copy.setCounter(si.functionCounter(copy.getOrdinal()));
            copy.subtract(original);
            list.add(copy);
         }

         rc.put(si, list);
      }

      return new NativeStats(rc);
   }

   public void dump(PrintStream ps) {
      boolean firstSI = true;

      for(Iterator i$ = this.snapshot.entrySet().iterator(); i$.hasNext(); ps.print("]")) {
         Entry<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> entry = (Entry)i$.next();
         NativeStats.StatsInterface si = (NativeStats.StatsInterface)entry.getKey();
         ArrayList<NativeStats.NativeFunction> funcs = (ArrayList)entry.getValue();
         int total = 0;

         NativeStats.NativeFunction func;
         for(Iterator i$ = funcs.iterator(); i$.hasNext(); total += func.getCounter()) {
            func = (NativeStats.NativeFunction)i$.next();
         }

         if (!firstSI) {
            ps.print(", ");
         }

         firstSI = false;
         ps.print("[");
         if (total > 0) {
            ps.println("{ ");
            ps.println("  \"class\": \"" + si.getNativeClass() + "\",");
            ps.println("  \"total\": " + total + ", ");
            ps.print("  \"functions\": {");
            boolean firstFunc = true;
            Iterator i$ = funcs.iterator();

            while(i$.hasNext()) {
               NativeStats.NativeFunction func = (NativeStats.NativeFunction)i$.next();
               if (func.getCounter() > 0) {
                  if (!firstFunc) {
                     ps.print(",");
                  }

                  firstFunc = false;
                  ps.println();
                  ps.print("    \"" + func.getName() + "\": " + func.getCounter());
               }
            }

            ps.println();
            ps.println("  }");
            ps.print("}");
         }
      }

   }

   private static HashMap<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> snapshot(Collection<NativeStats.StatsInterface> classes) {
      HashMap<NativeStats.StatsInterface, ArrayList<NativeStats.NativeFunction>> rc = new HashMap();
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         NativeStats.StatsInterface sc = (NativeStats.StatsInterface)i$.next();
         int count = sc.functionCount();
         ArrayList<NativeStats.NativeFunction> functions = new ArrayList(count);

         for(int i = 0; i < count; ++i) {
            String name = sc.functionName(i);
            functions.add(new NativeStats.NativeFunction(i, name, 0));
         }

         Collections.sort(functions);
         rc.put(sc, functions);
      }

      return rc;
   }

   public static class NativeFunction implements Comparable<NativeStats.NativeFunction> {
      private final int ordinal;
      private final String name;
      private int counter;

      public NativeFunction(int ordinal, String name, int callCount) {
         this.ordinal = ordinal;
         this.name = name;
         this.counter = callCount;
      }

      void subtract(NativeStats.NativeFunction func) {
         this.counter -= func.counter;
      }

      public int getCounter() {
         return this.counter;
      }

      public void setCounter(int counter) {
         this.counter = counter;
      }

      public String getName() {
         return this.name;
      }

      public int getOrdinal() {
         return this.ordinal;
      }

      public int compareTo(NativeStats.NativeFunction func) {
         return func.counter - this.counter;
      }

      public void reset() {
         this.counter = 0;
      }

      public NativeStats.NativeFunction copy() {
         return new NativeStats.NativeFunction(this.ordinal, this.name, this.counter);
      }
   }

   public interface StatsInterface {
      String getNativeClass();

      int functionCount();

      String functionName(int var1);

      int functionCounter(int var1);
   }
}
