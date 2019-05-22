package com.mks.api.response;

import java.util.Iterator;

public interface FieldContainer {
   Field getField(String var1);

   Iterator getFields();

   int getFieldListSize();

   boolean contains(String var1);
}
