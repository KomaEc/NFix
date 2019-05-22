package org.trie;

public class TrieJNI {
   private TrieJNI instance = null;

   public native boolean createTrie();

   public native void deleteTrie();

   public native boolean searchTrie(int[] var1);

   public native void insertTrie(int[] var1);

   public boolean create() {
      if (this.instance == null) {
         this.instance = new TrieJNI();
      }

      return this.instance.createTrie();
   }

   public void destroy() {
      this.instance.deleteTrie();
   }

   private boolean _search(int[] var1) {
      return this.instance.searchTrie(var1);
   }

   public boolean search(String var1) {
      int[] var2 = this.getIntArray(var1);
      return this._search(var2);
   }

   private void _insert(int[] var1) {
      this.instance.insertTrie(var1);
   }

   public void insert(String var1) {
      int[] var2 = this.getIntArray(var1);
      this._insert(var2);
   }

   private int[] getIntArray(String var1) {
      String[] var4;
      int[] var2 = new int[(var4 = var1.split(",")).length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = Integer.parseInt(var4[var3]);
      }

      return var2;
   }

   static {
      String var0 = System.getProperty("os.name").toLowerCase();
      String var1 = System.getProperty("os.arch");
      if (var0.contains("windows")) {
         if (var1.contains("64")) {
            System.loadLibrary("msvcr110d");
         } else {
            System.loadLibrary("msvcr100");
         }
      }

      System.loadLibrary("TrieJNI");
   }
}
