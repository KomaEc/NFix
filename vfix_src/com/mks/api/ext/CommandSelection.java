package com.mks.api.ext;

public abstract class CommandSelection {
   public abstract int getSize();

   public abstract String nextItem();

   public abstract boolean hasNext();

   public abstract void startSelection();

   public abstract void endSelection();

   public String[] toArgs() {
      int size = this.getSize();
      String[] args = new String[size];
      this.startSelection();

      for(int i = 0; i < size; ++i) {
         args[i] = this.nextItem();
      }

      return args;
   }
}
