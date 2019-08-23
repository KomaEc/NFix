# pdfbox-2965

## Patch
```diff
--- src/main/java/org/apache/pdfbox/pdmodel/interactive/form/PDAcroForm.java	2019-08-21 09:49:47.092294839 +0800
+++ npe.patch	2019-08-21 09:55:07.901866878 +0800
@@ -1,3 +1,4 @@
+
 /*
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
@@ -234,9 +235,8 @@
      *
      * @param fullyQualifiedName The name of the field to get.
      * @return The field with that name of null if one was not found.
-     * @throws IOException If there is an error getting the field type.
      */
-    public PDField getField(String fullyQualifiedName) throws IOException
+    public PDField getField(String fullyQualifiedName)
     {
         PDField retval = null;
         if (fieldCache != null)
@@ -248,35 +248,38 @@
             String[] nameSubSection = fullyQualifiedName.split("\\.");
             COSArray fields = (COSArray) dictionary.getDictionaryObject(COSName.FIELDS);
 
-            for (int i = 0; i < fields.size() && retval == null; i++)
+            if (fields != null)
             {
-                COSDictionary element = (COSDictionary) fields.getObject(i);
-                if (element != null)
+                for (int i = 0; i < fields.size() && retval == null; i++)
                 {
-                    COSString fieldName =
-                        (COSString)element.getDictionaryObject(COSName.T);
-                    if (fieldName.getString().equals(fullyQualifiedName) ||
-                        fieldName.getString().equals(nameSubSection[0]))
+                    COSDictionary element = (COSDictionary) fields.getObject(i);
+                    if (element != null)
                     {
-                        PDField root = PDField.fromDictionary(this, element, null);
-                        if (root != null)
+                        COSString fieldName =
+                            (COSString)element.getDictionaryObject(COSName.T);
+                        if (fieldName.getString().equals(fullyQualifiedName) ||
+                            fieldName.getString().equals(nameSubSection[0]))
                         {
-                            if (nameSubSection.length > 1)
+                            PDField root = PDField.fromDictionary(this, element, null);
+                            if (root != null)
                             {
-                                PDField kid = root.findKid(nameSubSection, 1);
-                                if (kid != null)
+                                if (nameSubSection.length > 1)
                                 {
-                                    retval = kid;
+                                    PDField kid = root.findKid(nameSubSection, 1);
+                                    if (kid != null)
+                                    {
+                                        retval = kid;
+                                    }
+                                    else
+                                    {
+                                        retval = root;
+                                    }
                                 }
                                 else
                                 {
                                     retval = root;
                                 }
                             }
-                            else
-                            {
-                                retval = root;
-                            }
                         }
                     }
                 }
@@ -469,4 +472,4 @@
     {
         dictionary.setFlag(COSName.SIG_FLAGS, FLAG_APPEND_ONLY, appendOnly);
     }
-}
+}
\ No newline at end of file
```