package com.mks.api.response;

import java.util.Iterator;
import java.util.List;

public interface ItemList extends List {
   Item getItem(String var1);

   Iterator getItems();

   int getItemListSize();

   boolean contains(String var1);

   boolean contains(String var1, String var2, String var3);
}
