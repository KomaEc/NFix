# drools-2757

## Commit
3d61812d1ef5b5a7a052c7a5af8e852c871b9458
parent : c78d397

## Infer
Can't find relevant places

## Type
Add Null Check

## Patch
```diff
--- ./src/main/java/org/drools/compiler/kie/builder/impl/AbstractKieProject.java	2019-06-18 15:19:41.047961471 +0800
+++ patch.txt	2019-06-18 15:34:07.192398246 +0800
@@ -77,7 +77,11 @@
 
     public void verify(String[] kBaseNames, ResultsImpl messages) {
         for ( String modelName : kBaseNames ) {
-            buildKnowledgePackages( (KieBaseModelImpl) kBaseModels.get( modelName ), messages);
+            KieBaseModelImpl kieBaseModel = (KieBaseModelImpl) kBaseModels.get( modelName );
+            if ( kieBaseModel == null ) {
+                throw new RuntimeException( "Unknown KieBase. Cannot find a KieBase named: " + modelName );
+            }
+            buildKnowledgePackages( kieBaseModel, messages);
         }
     }
 
```