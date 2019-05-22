package soot.jimple.spark.geom.dataRep;

import java.io.PrintStream;

public abstract class ShapeNode {
   public long I1;
   public long I2;
   public long E1;
   public boolean is_new = true;
   public ShapeNode next = null;

   public abstract ShapeNode makeDuplicate();

   public abstract boolean inclusionTest(ShapeNode var1);

   public abstract boolean coverThisXValue(long var1);

   public abstract void printSelf(PrintStream var1);

   public abstract void copy(ShapeNode var1);
}
