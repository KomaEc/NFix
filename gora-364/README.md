# gora-364

## Commit
89c025d2f9e4ba19378eb15537104c8775bf2474

Parent : 22b5cf8e5bcd0c974167a1a5cf03a0771f30790a

## Patch
```diff
--- src/main/java/org/apache/gora/memory/store/MemStore.java	2019-06-25 14:56:57.862485788 +0800
+++ patch.txt	2019-06-25 15:03:47.958745122 +0800
@@ -139,6 +139,9 @@
   @Override
   public T get(K key, String[] fields) {
     T obj = map.get(key);
+    if (obj == null) {
+      return null;
+    }
     return getPersistent(obj, getFieldsToQuery(fields));
   }
 
@@ -204,4 +207,4 @@
 
   @Override
   public void flush() { }
-}
+}
\ No newline at end of file
```