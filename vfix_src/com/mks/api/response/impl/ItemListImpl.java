package com.mks.api.response.impl;

import com.mks.api.response.Item;
import com.mks.api.response.modifiable.ModifiableItemList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ItemListImpl extends ArrayList implements ModifiableItemList {
   ItemListImpl() {
   }

   public void add(Item item) {
      super.add(item);
   }

   public Item getItem(String id) {
      Iterator it = super.iterator();

      Item item;
      do {
         if (!it.hasNext()) {
            throw new NoSuchElementException(id);
         }

         item = (Item)it.next();
      } while(!item.getId().equals(id));

      return item;
   }

   public Iterator getItems() {
      return super.iterator();
   }

   public int getItemListSize() {
      return super.size();
   }

   public boolean contains(String id) {
      Iterator it = this.iterator();

      Item i;
      do {
         if (!it.hasNext()) {
            return false;
         }

         i = (Item)it.next();
      } while(!i.getId().equals(id));

      return true;
   }

   public boolean contains(String id, String context, String modelType) {
      return this.contains((Object)(new ItemImpl(id, context, modelType)));
   }

   public boolean contains(Object o) {
      return o instanceof String ? this.contains((String)o) : super.contains(o);
   }
}
