package com.google.common.collect;

class TreeTraverser$4 extends FluentIterable<T> {
   // $FF: synthetic field
   final Object val$root;
   // $FF: synthetic field
   final TreeTraverser this$0;

   TreeTraverser$4(TreeTraverser this$0, Object var2) {
      this.this$0 = this$0;
      this.val$root = var2;
   }

   public UnmodifiableIterator<T> iterator() {
      return this.this$0.new BreadthFirstIterator(this.val$root);
   }
}
