package com.gzoltar.client.trie;

import java.util.Iterator;
import java.util.TreeSet;

public class TrieNode implements Comparable<Object> {
   private Integer value;
   private TrieNode father;
   private TreeSet<TrieNode> children;

   public TrieNode() {
      this((Integer)null);
   }

   public TrieNode(Integer var1) {
      this.value = var1;
      this.father = null;
      this.children = new TreeSet();
   }

   public TrieNode(String var1) {
      this();
      String[] var7 = var1.split(",");
      TrieNode var2 = this;
      int var3 = (var7 = var7).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var7[var4];
         TrieNode var6 = new TrieNode(Integer.valueOf(var5));
         var2.addChild(var6);
         var2 = var6;
      }

   }

   public Integer getValue() {
      return this.value;
   }

   public TrieNode getFather() {
      return this.father;
   }

   public TrieNode getFirstChild() {
      assert this.children.size() > 0;

      return (TrieNode)this.children.first();
   }

   public TreeSet<TrieNode> getChildren() {
      return this.children;
   }

   public void addChild(TrieNode var1) {
      var1.father = this;
      this.children.add(var1);
   }

   public void deleteChildren() {
      this.children.clear();
   }

   public int compareTo(Object var1) {
      TrieNode var2 = (TrieNode)var1;
      return this.value == null && var2.value == null ? 0 : Integer.compare(this.value, var2.value);
   }

   public String toString() {
      StringBuilder var1;
      (var1 = new StringBuilder()).append("Value: " + this.value + "\n");
      var1.append("Children: " + this.children.toString());
      return var1.toString();
   }

   public String toString(String var1) {
      StringBuilder var2;
      (var2 = new StringBuilder()).append(var1 + this.value);
      Iterator var3 = this.getChildren().iterator();

      while(var3.hasNext()) {
         TrieNode var4 = (TrieNode)var3.next();
         var2.append(var4.toString(var1 + " "));
      }

      return var2.toString();
   }
}
