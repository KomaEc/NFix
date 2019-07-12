# dubbo-4218

## Patch
```diff
--- src/main/java/org/apache/dubbo/rpc/cluster/router/tag/model/TagRouterRule.java	2019-07-12 09:28:20.684149014 +0800
+++ npe.patch	2019-07-12 09:51:57.767107477 +0800
@@ -16,6 +16,7 @@
  */
 package org.apache.dubbo.rpc.cluster.router.tag.model;
 
+import org.apache.dubbo.common.utils.CollectionUtils;
 import org.apache.dubbo.rpc.cluster.router.AbstractRouterRule;
 
 import java.util.ArrayList;
@@ -50,7 +51,7 @@
             return;
         }
 
-        tags.forEach(tag -> {
+        tags.stream().filter(tag -> CollectionUtils.isNotEmpty(tag.getAddresses())).forEach(tag -> {
             tagnameToAddresses.put(tag.getName(), tag.getAddresses());
             tag.getAddresses().forEach(addr -> {
                 List<String> tagNames = addressToTagnames.computeIfAbsent(addr, k -> new ArrayList<>());
@@ -60,7 +61,10 @@
     }
 
     public List<String> getAddresses() {
-        return tags.stream().flatMap(tag -> tag.getAddresses().stream()).collect(Collectors.toList());
+        return tags.stream()
+                .filter(tag -> CollectionUtils.isNotEmpty(tag.getAddresses()))
+                .flatMap(tag -> tag.getAddresses().stream())
+                .collect(Collectors.toList());
     }
 
     public List<String> getTagNames() {
@@ -83,4 +87,4 @@
     public void setTags(List<Tag> tags) {
         this.tags = tags;
     }
-}
+}
\ No newline at end of file
```