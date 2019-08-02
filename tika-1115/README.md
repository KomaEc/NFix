# tika-1115

## Patch
```diff
--- src/main/java/org/apache/tika/parser/image/ImageMetadataExtractor.java	2019-07-24 14:29:23.066864756 +0800
+++ npe.patch	2019-07-24 14:39:36.946400855 +0800
@@ -388,11 +388,13 @@
             }
             if (directory.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
                 Date datetime = directory.getDate(ExifIFD0Directory.TAG_DATETIME);
-                String datetimeNoTimeZone = DATE_UNSPECIFIED_TZ.format(datetime);
-                metadata.set(TikaCoreProperties.MODIFIED, datetimeNoTimeZone);
-                // If Date/Time Original does not exist this might be creation date
-                if (metadata.get(TikaCoreProperties.CREATED) == null) {
-                    metadata.set(TikaCoreProperties.CREATED, datetimeNoTimeZone);
+                if (datetime != null) {
+                    String datetimeNoTimeZone = DATE_UNSPECIFIED_TZ.format(datetime);
+                    metadata.set(TikaCoreProperties.MODIFIED, datetimeNoTimeZone);
+                    // If Date/Time Original does not exist this might be creation date
+                    if (metadata.get(TikaCoreProperties.CREATED) == null) {
+                        metadata.set(TikaCoreProperties.CREATED, datetimeNoTimeZone);
+                    }
                 }
             }
         }
@@ -450,4 +452,4 @@
         }
     }
 
-}
+}
\ No newline at end of file
```