# math-305

## Commit
ef9b639afc3dd8d1c35eab23cc9cb6b3a6a2c858

Parent : 9e6576e7cd4ad17185c5102fd3c6f9f9a6941ed4

## Patch
```diff
--- src/main/java/org/apache/commons/math/util/MathUtils.java	2019-06-25 00:27:38.449248028 +0800
+++ patch.txt	2019-06-25 00:30:34.193255258 +0800
@@ -1621,9 +1621,9 @@
      * @return the L<sub>2</sub> distance between the two points
      */
     public static double distance(int[] p1, int[] p2) {
-      int sum = 0;
+      double sum = 0;
       for (int i = 0; i < p1.length; i++) {
-          final int dp = p1[i] - p2[i];
+          final double dp = p1[i] - p2[i];
           sum += dp * dp;
       }
       return Math.sqrt(sum);
@@ -1660,4 +1660,4 @@
     }
 
 
-}
+}
```