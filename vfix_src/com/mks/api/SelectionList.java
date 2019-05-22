package com.mks.api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SelectionList {
   private List list = new LinkedList();

   public void add(String selection) {
      this.list.add(selection);
   }

   public void add(SelectionList selList) {
      this.list.addAll(selList.list);
   }

   public Iterator getSelections() {
      return this.list.iterator();
   }

   public String getSelection(int idx) {
      return (String)this.list.get(idx);
   }

   public int size() {
      return this.list.size();
   }

   public void clear() {
      this.list.clear();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() != o.getClass()) {
         SelectionList other = (SelectionList)o;
         return this.list.equals(other.list);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.list.hashCode();
   }
}
