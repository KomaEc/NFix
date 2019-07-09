# math-988

## patch 1
```diff
--- src/main/java/org/apache/commons/math3/geometry/euclidean/threed/SubLine.java	2019-07-09 09:55:32.993733211 +0800
+++ npe1.patch	2019-07-09 09:55:59.080518324 +0800
@@ -111,6 +111,9 @@
 
         // compute the intersection on infinite line
         Vector3D v1D = line.intersection(subLine.line);
+        if (v1D == null) {
+            return null;
+        }
 
         // check location of point with respect to first sub-line
         Location loc1 = remainingRegion.checkPoint(line.toSubSpace(v1D));
@@ -139,4 +142,4 @@
                                 line.toSubSpace(end).getX());
     }
 
-}
+}
\ No newline at end of file
```

## patch 2
```diff
--- src/main/java/org/apache/commons/math3/geometry/euclidean/twod/SubLine.java	2019-07-09 09:55:32.993733211 +0800
+++ npe2.patch	2019-07-09 09:57:02.993640130 +0800
@@ -115,6 +115,9 @@
 
         // compute the intersection on infinite line
         Vector2D v2D = line1.intersection(line2);
+        if (v2D == null) {
+            return null;
+        }
 
         // check location of point with respect to first sub-line
         Location loc1 = getRemainingRegion().checkPoint(line1.toSubSpace(v2D));
@@ -206,4 +209,4 @@
 
     }
 
-}
+}
\ No newline at end of file
```
