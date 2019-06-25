# drools-1064

## Commit
commit cb94ef640006fded55289f871703ccba7336892e

## VFix
N/A, cross module!

## Infer
Find the position of repair (report as potential null dereference)

## Type
Add null check

## Bad
NPE position is in another module

## Patch
```diff
--- src/main/java/org/drools/compiler/rule/builder/PatternBuilder.java	2019-06-18 14:10:01.366977938 +0800
+++ patch.txt	2019-06-18 14:10:51.204590672 +0800
@@ -979,7 +979,7 @@
 
     private boolean isDateType( RuleBuildContext context, Pattern pattern, String leftValue ) {
         Declaration declaration = pattern.getDeclarations().get( leftValue );
-        if (declaration != null) {
+        if (declaration != null && declaration.getExtractor() != null) {
             return declaration.getValueType() == ValueType.DATE_TYPE;
         }
 
@@ -1927,4 +1927,4 @@
         }
         return result;
     }
-}
+}
```