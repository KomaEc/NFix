# dubbo-4299

## Patch
```diff
--- src/main/java/org/apache/dubbo/common/utils/PojoUtils.java	2019-07-10 09:14:34.530548071 +0800
+++ npe.patch	2019-07-09 17:26:14.263415615 +0800
@@ -366,7 +366,7 @@
                 history.put(pojo, dest);
                 for (Object obj : src) {
                     Type keyType = getGenericClassByIndex(genericType, 0);
-                    Class<?> keyClazz = obj.getClass();
+                    Class<?> keyClazz = obj == null ? null : obj.getClass();
                     if (keyType instanceof Class) {
                         keyClazz = (Class<?>) keyType;
                     }
```