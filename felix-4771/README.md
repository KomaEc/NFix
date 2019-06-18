# felix-4771

## Commit
commit 3c9335dbaeff3e28608d9f437d1b1500dd20c652

parent : 863bca7

## Type
.clone() -> return directly

## Modifed
pom.xml modified. Add maven compile plugin to change the version from 1.3 to 1.8

## Patch
```diff
--- ./src/main/java/org/apache/felix/metatype/Attribute.java	2019-06-18 19:05:10.877441054 +0800
+++ patch.txt	2019-06-18 19:08:32.421534232 +0800
@@ -41,7 +41,9 @@
 
     public String[] getContent()
     {
-        return ( String[] ) content.clone();
+        // FELIX-4771 - removed the clone as we're already working on a local 
+        // copy and this pattern isn't used in other parts of the API...
+        return this.content;
     }
 
     public void addContent(String[] added)

```

## ...
Use Intellij