# felix-2432

## Commit
57b9cbcbb208c51e7dcfeceddb30aca1365e678e

Parent : cf7d7a6

## Type
Add Null Check. Return string "null"

## Patch
```diff
--- ./src/main/java/org/apache/felix/gogo/runtime/Reflective.java	2019-06-18 16:43:58.872136428 +0800
+++ patch.txt	2019-06-18 16:54:12.024614377 +0800
@@ -171,7 +171,7 @@
                 {
                     params.append(", ");
                 }
-                params.append(arg.getClass().getSimpleName());
+                params.append(arg == null ? "null" : arg.getClass().getSimpleName());
             }
 
             throw new IllegalArgumentException(String.format(
@@ -275,6 +275,13 @@
                 else
                 {
                     out[i] = coerce(session, target, types[i], in.get(0));
+                    
+                    if (out[i] == null && types[i].isArray() && in.size() > 0)
+                    {
+                        // don't coerce null to array FELIX-2432
+                        out[i] = NO_MATCH;
+                    }
+                    
                     if (out[i] != NO_MATCH)
                     {
                         in.remove(0);

```