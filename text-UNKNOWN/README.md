# common-text-UNKNOWN

## Patch
```diff
--- src/main/java/org/apache/commons/text/translate/AggregateTranslator.java	2019-09-06 10:16:33.940152071 +0800
+++ npe.patch	2019-09-06 10:18:05.146336291 +0800
@@ -18,6 +18,8 @@
 
 import java.io.IOException;
 import java.io.Writer;
+import java.util.ArrayList;
+import java.util.List;
 
 /**
  * Executes a sequence of translators one after the other. Execution ends whenever 
@@ -27,7 +29,7 @@
  */
 public class AggregateTranslator extends CharSequenceTranslator {
 
-    private final CharSequenceTranslator[] translators;
+    private final List<CharSequenceTranslator> translators = new ArrayList<>();
 
     /**
      * Specify the translators to be used at creation time. 
@@ -36,9 +38,11 @@
      */
     public AggregateTranslator(final CharSequenceTranslator... translators) {
         if (translators != null) {
-            this.translators = translators.clone();
-        } else {
-            this.translators = null;
+            for (CharSequenceTranslator translator : translators) {
+                if (translator != null) {
+                    this.translators.add(translator);
+                }
+            }
         }
     }
 
@@ -58,4 +62,4 @@
         return 0;
     }
 
-}
+}
\ No newline at end of file
```