# tika-1011

## Patch
```diff
--- src/main/java/org/apache/tika/parser/txt/Icu4jEncodingDetector.java	2019-08-04 08:43:54.872479887 +0800
+++ npe.patch	2019-08-04 08:46:56.863000000 +0800
@@ -1,3 +1,4 @@
+
 /**
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
@@ -46,7 +47,12 @@
         }
 
         if (incomingCharset != null) {
-            detector.setDeclaredEncoding(CharsetUtils.clean(incomingCharset));
+            String cleaned = CharsetUtils.clean(incomingCharset);
+            if (cleaned != null) {
+                detector.setDeclaredEncoding(cleaned);
+            } else {
+                // TODO: log a warning?
+            }
         }
 
         // TIKA-341 without enabling input filtering (stripping of tags)
@@ -66,4 +72,4 @@
         return null;
     }
 
-}
+}
\ No newline at end of file
```