package soot.jimple.spark.geom.geomPA;

public interface IWorklist {
   void initialize(int var1);

   boolean has_job();

   IVarAbstraction next();

   void push(IVarAbstraction var1);

   int size();

   void clear();
}
