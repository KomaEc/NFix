# tika-6000

## Patch
```diff
--- src/main/java/org/apache/tika/parser/odf/OpenDocumentContentParser.java	2019-08-02 11:00:18.639453991 +0800
+++ npe.patch	2019-08-02 11:04:20.145703709 +0800
@@ -1,3 +1,4 @@
+  
 /*
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
@@ -169,6 +170,9 @@
 		    }
 
             TextStyle style = textStyleMap.get(name);
+            if (style == null) {
+              return;
+            }
 
             // End tags that refer to no longer valid styles
             if (!style.underlined && lastTextStyle != null && lastTextStyle.underlined) {
@@ -224,7 +228,7 @@
 		        String namespaceURI, String localName, String qName,
 		        Attributes attrs) throws SAXException {
 		    // keep track of current node type. If it is a text node,
-		    // a bit at the current depth ist set in textNodeStack.
+		    // a bit at the current depth its set in textNodeStack.
 		    // characters() checks the top bit to determine, if the
 		    // actual node is a text node to print out nodeDepth contains
 		    // the depth of the current node and also marks top of stack.
```