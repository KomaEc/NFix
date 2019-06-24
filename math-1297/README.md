# math-1297

## Commit
564345179f3f33d249daca36f0054773996d5782

Parent : c9b1c8f9662f865a613632e1d390922050130b60

## Patch
```diff
--- src/main/java/org/apache/commons/math4/ode/MultistepIntegrator.java	2019-06-25 00:37:08.861271493 +0800
+++ patch.txt	2019-06-25 00:41:24.457282008 +0800
@@ -18,6 +18,7 @@
 package org.apache.commons.math4.ode;
 
 import org.apache.commons.math4.exception.DimensionMismatchException;
+import org.apache.commons.math4.exception.MathIllegalStateException;
 import org.apache.commons.math4.exception.MaxCountExceededException;
 import org.apache.commons.math4.exception.NoBracketingException;
 import org.apache.commons.math4.exception.NumberIsTooSmallException;
@@ -248,6 +249,9 @@
                 }, t0, y0, t, new double[y0.length]);
             }
 
+            // we should not reach this step
+            throw new MathIllegalStateException(LocalizedFormats.MULTISTEP_STARTER_STOPPED_EARLY);
+
         } catch (InitializationCompletedMarkerException icme) { // NOPMD
             // this is the expected nominal interruption of the start integrator
 
@@ -458,4 +462,4 @@
 
     }
 
-}
+}
```