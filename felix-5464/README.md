# felix-5464

## Note
Modify the `processClass` method of the class `ClassScanner` to be visible.

## Patch
```diff
--- src/main/java/org/apache/felix/scrplugin/helper/ClassScanner.java	2019-08-17 19:37:03.011834155 +0800
+++ npe.patch	2019-08-17 19:40:21.039427523 +0800
@@ -197,7 +197,7 @@
     /**
      * Scan a single class.
      */
-    public ClassDescription processClass(final Class<?> annotatedClass, final String location)
+    private ClassDescription processClass(final Class<?> annotatedClass, final String location)
             throws SCRDescriptorFailureException, SCRDescriptorException {
         log.debug("Processing " + annotatedClass.getName());
         try {
@@ -208,7 +208,9 @@
             try {
                 classReader = new ClassReader(input);
             } finally {
-                input.close();
+                if ( input != null ) {
+                    input.close();
+                }
             }
             final ClassNode classNode = new ClassNode();
             classReader.accept(classNode, SKIP_CODE | SKIP_DEBUG | SKIP_FRAMES);
@@ -667,4 +669,4 @@
             }
         }
     }
-}
+}
\ No newline at end of file
```