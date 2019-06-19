# tika-1378

## Commit
65aea2b06b33c6b53999b6c52e017c38bf2af0b4

Parent : ed355dd561963ab4eb6cc9423e4b86e4e817f26e

## VFix
Partial success. (Add one null check)

## Infer
Fail to locate

## type
Add two null check

## Patch
```diff
--- ./src/main/java/org/apache/tika/language/translate/MicrosoftTranslator.java	2019-06-19 00:22:21.560490527 +0800
+++ patch.txt	2019-06-19 00:24:05.759990436 +0800
@@ -56,7 +56,7 @@
                 props.load(stream);
                 clientId = props.getProperty(ID_PROPERTY);
                 clientSecret = props.getProperty(SECRET_PROPERTY);
-                if (!clientId.equals(DEFAULT_ID) && !clientSecret.equals(DEFAULT_SECRET)) available = true;
+                this.available = checkAvailable();   
             }
         } catch (IOException e) {
         	e.printStackTrace();
@@ -119,7 +119,7 @@
      */
     public void setId(String id){
     	this.clientId = id;
-        if (!clientId.equals(DEFAULT_ID) && !clientSecret.equals(DEFAULT_SECRET)) available = true;
+        this.available = checkAvailable();   
     }
     
     /**
@@ -128,6 +128,13 @@
      */
     public void setSecret(String secret){
     	this.clientSecret = secret;
-        if (!clientId.equals(DEFAULT_ID) && !clientSecret.equals(DEFAULT_SECRET)) available = true;    	
+        this.available = checkAvailable();   	
     }
-}
+    
+    private boolean checkAvailable(){
+       return clientId != null && 
+    		   !clientId.equals(DEFAULT_ID) && 
+    		   clientSecret != null && 
+    		   !clientSecret.equals(DEFAULT_SECRET);
+    }
+}
\ No newline at end of file

```