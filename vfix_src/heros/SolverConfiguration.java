package heros;

public interface SolverConfiguration {
   boolean followReturnsPastSeeds();

   boolean autoAddZero();

   int numThreads();

   boolean computeValues();

   boolean recordEdges();
}
