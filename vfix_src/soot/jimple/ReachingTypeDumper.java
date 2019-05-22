package soot.jimple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.PointsToAnalysis;
import soot.RefLikeType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

public class ReachingTypeDumper {
   protected PointsToAnalysis pa;
   protected String output_dir;

   public ReachingTypeDumper(PointsToAnalysis pa, String output_dir) {
      this.pa = pa;
      this.output_dir = output_dir;
   }

   public void dump() {
      try {
         PrintWriter file = new PrintWriter(new FileOutputStream(new File(this.output_dir, "types")));
         Iterator var2 = Scene.v().getApplicationClasses().iterator();

         SootClass cls;
         while(var2.hasNext()) {
            cls = (SootClass)var2.next();
            this.handleClass(file, cls);
         }

         var2 = Scene.v().getLibraryClasses().iterator();

         while(var2.hasNext()) {
            cls = (SootClass)var2.next();
            this.handleClass(file, cls);
         }

         file.close();
      } catch (IOException var4) {
         throw new RuntimeException("Couldn't dump reaching types." + var4);
      }
   }

   protected void handleClass(PrintWriter out, SootClass c) {
      Iterator var3 = c.getMethods().iterator();

      while(true) {
         SootMethod m;
         do {
            if (!var3.hasNext()) {
               return;
            }

            m = (SootMethod)var3.next();
         } while(!m.isConcrete());

         Body b = m.retrieveActiveBody();
         Local[] sortedLocals = (Local[])b.getLocals().toArray(new Local[b.getLocalCount()]);
         Arrays.sort(sortedLocals, new ReachingTypeDumper.StringComparator());
         Local[] var7 = sortedLocals;
         int var8 = sortedLocals.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Local l = var7[var9];
            out.println("V " + m + l);
            if (l.getType() instanceof RefLikeType) {
               Set<Type> types = this.pa.reachingObjects(l).possibleTypes();
               Type[] sortedTypes = (Type[])types.toArray(new Type[types.size()]);
               Arrays.sort(sortedTypes, new ReachingTypeDumper.StringComparator());
               Type[] var13 = sortedTypes;
               int var14 = sortedTypes.length;

               for(int var15 = 0; var15 < var14; ++var15) {
                  Type type = var13[var15];
                  out.println("T " + type);
               }
            }
         }
      }
   }

   private static class StringComparator<T> implements Comparator<T> {
      private StringComparator() {
      }

      public int compare(T o1, T o2) {
         return o1.toString().compareTo(o2.toString());
      }

      // $FF: synthetic method
      StringComparator(Object x0) {
         this();
      }
   }
}
