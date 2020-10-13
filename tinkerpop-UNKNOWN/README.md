# tinkerpop-UNKNOWN

## Patch
```diff
--- src/main/java/org/apache/tinkerpop/gremlin/process/traversal/strategy/optimization/PrunePathStrategy.java	2019-09-05 18:27:09.832440965 +0800
+++ npe.patch	2019-09-05 18:40:16.170320680 +0800
@@ -1,3 +1,4 @@
+  
 /*
  * Licensed to the Apache Software Foundation (ASF) under one
  * or more contributor license agreements.  See the NOTICE file
@@ -90,7 +91,7 @@
                 do {
                     // if this is the top level traversal, propagate all nested labels
                     // to previous PathProcess steps
-                    if (step instanceof PathProcessor) {
+                    if (step instanceof PathProcessor && null != ((PathProcessor) step).getKeepLabels()) {
                         ((PathProcessor) step).getKeepLabels().addAll(referencedLabels);
                     }
                     step = step.getPreviousStep();
@@ -167,4 +168,4 @@
     public Set<Class<? extends OptimizationStrategy>> applyPrior() {
         return PRIORS;
     }
-}
+}
\ No newline at end of file
```