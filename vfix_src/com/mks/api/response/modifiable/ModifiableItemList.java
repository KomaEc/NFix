package com.mks.api.response.modifiable;

import com.mks.api.response.Item;
import com.mks.api.response.ItemList;

public interface ModifiableItemList extends ItemList {
   void add(Item var1);
}
