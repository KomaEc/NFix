package com.gzoltar.client.trie;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Trie {
   private TrieNode root = new TrieNode();

   public TrieNode getRoot() {
      return this.root;
   }

   public boolean insert(TrieNode var1) {
      if (this.isSubsumed(var1)) {
         return false;
      } else {
         List var2;
         if ((var2 = this.search(this.root, var1)).get(0) == null && var2.get(1) == null) {
            return false;
         } else if (var2.get(0) == null && var2.get(1) != null) {
            return true;
         } else {
            ((TrieNode)var2.get(0)).addChild((TrieNode)var2.get(1));
            return true;
         }
      }
   }

   private List<TrieNode> search(TrieNode var1, TrieNode var2) {
      if (var1.getValue() == var2.getValue()) {
         if (var1.getChildren().isEmpty() && !var2.getChildren().isEmpty()) {
            return Arrays.asList(var1, var2.getFirstChild());
         } else if (var1.getChildren().isEmpty() && var2.getChildren().isEmpty()) {
            return Arrays.asList(null, null);
         } else if (!var1.getChildren().isEmpty() && var2.getChildren().isEmpty()) {
            var1.deleteChildren();
            return Arrays.asList(null, var2);
         } else {
            List var3 = null;
            Iterator var5 = var1.getChildren().iterator();

            TrieNode var4;
            do {
               if (!var5.hasNext()) {
                  assert var3 != null;

                  return var3;
               }

               var4 = (TrieNode)var5.next();
            } while((var3 = this.search(var4, var2.getFirstChild())).get(0) == var4.getFather());

            return var3;
         }
      } else {
         return Arrays.asList(var1.getFather(), var2);
      }
   }

   public boolean isSubsumed(TrieNode var1) {
      Iterator var2 = this.root.getChildren().iterator();

      TrieNode var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (TrieNode)var2.next();
      } while(!this._isSubsumed(var3, var1.getFirstChild()));

      return true;
   }

   private boolean _isSubsumed(TrieNode var1, TrieNode var2) {
      while(true) {
         if (var1.getValue() == var2.getValue()) {
            if (var1.getChildren().isEmpty() && !var2.getChildren().isEmpty()) {
               return true;
            }

            if (var1.getChildren().isEmpty() && var2.getChildren().isEmpty()) {
               return true;
            }

            if (!var1.getChildren().isEmpty() && var2.getChildren().isEmpty()) {
               return false;
            }

            Iterator var3 = var1.getChildren().iterator();

            while(var3.hasNext()) {
               TrieNode var4 = (TrieNode)var3.next();
               if (this._isSubsumed(var4, var2.getFirstChild())) {
                  return true;
               }
            }
         }

         if (var2.getChildren().isEmpty()) {
            return false;
         }

         var2 = var2.getFirstChild();
         var1 = var1;
         this = this;
      }
   }
}
