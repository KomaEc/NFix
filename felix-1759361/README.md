# felix-1759361

## Patch
```diff
--- src/main/java/org/apache/felix/converter/impl/AdapterImpl.java	2019-07-26 18:54:28.871161176 +0800
+++ npe.patch	2019-07-26 18:59:13.748480814 +0800
@@ -65,8 +65,11 @@
         if (fromCls.equals(toCls))
             throw new IllegalArgumentException();
 
-        classRules.put(new TypePair(fromCls, toCls), (ConvertFunction<Object, Object>) toFun);
-        classRules.put(new TypePair(toCls, fromCls), (ConvertFunction<Object, Object>) fromFun);
+        if (toFun != null)
+            classRules.put(new TypePair(fromCls, toCls), (ConvertFunction<Object, Object>) toFun);
+
+        if (fromFun != null)
+            classRules.put(new TypePair(toCls, fromCls), (ConvertFunction<Object, Object>) fromFun);
         return this;
     }
 
@@ -226,4 +229,4 @@
                     Objects.equals(to, o.to);
         }
     }
-}
+}
\ No newline at end of file

```