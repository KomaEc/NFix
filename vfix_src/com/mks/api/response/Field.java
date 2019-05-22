package com.mks.api.response;

import java.util.Date;
import java.util.List;

public interface Field {
   String BOOLEAN_TYPE = "java.lang.Boolean";
   String DATE_TYPE = "java.util.Date";
   String DOUBLE_TYPE = "java.lang.Double";
   String FLOAT_TYPE = "java.lang.Float";
   String INTEGER_TYPE = "java.lang.Integer";
   String LONG_TYPE = "java.lang.Long";
   String STRING_TYPE = "java.lang.String";
   String ITEM_TYPE = "com.mks.api.response.Item";
   String VALUE_LIST_TYPE = "com.mks.api.response.ValueList";
   String ITEM_LIST_TYPE = "com.mks.api.response.ItemList";

   String getName();

   Object getValue();

   String getModelType();

   String getDataType();

   Boolean getBoolean();

   Date getDateTime();

   Double getDouble();

   Float getFloat();

   Integer getInteger();

   Long getLong();

   String getString();

   List getList();

   Item getItem();

   String getValueAsString();

   String getDisplayName();
}
