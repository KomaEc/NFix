# Math 369

## Patch
```diff
--- src/main/java/org/apache/commons/math/analysis/solvers/BisectionSolver.java	2019-08-16 10:38:41.673029348 +0800
+++ npe.patch	2019-08-16 10:54:05.231714635 +0800
@@ -26,7 +26,7 @@
  * <p>
  * The function should be continuous but not necessarily smooth.</p>
  *
- * @version $Revision: 811685 $ $Date: 2009-09-05 19:36:48 +0200 (sam., 05 sept. 2009) $
+ * @version $Revision$ $Date$
  */
 public class BisectionSolver extends UnivariateRealSolverImpl {
 
@@ -69,7 +69,7 @@
     /** {@inheritDoc} */
     public double solve(final UnivariateRealFunction f, double min, double max, double initial)
         throws MaxIterationsExceededException, FunctionEvaluationException {
-        return solve(min, max);
+        return solve(f, min, max);
     }
 
     /** {@inheritDoc} */
@@ -106,4 +106,4 @@
 
         throw new MaxIterationsExceededException(maximalIterationCount);
     }
-}
+}
\ No newline at end of file
```