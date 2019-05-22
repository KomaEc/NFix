package com.mks.api.response.modifiable;

public interface ResponseFactory {
   ModifiableResponse createResponse();

   ModifiableWorkItem createWorkItem(String var1, String var2, String var3);

   ModifiableSubRoutine createSubRoutine(String var1);

   ModifiableItem createItem(String var1, String var2, String var3);

   ModifiableResult createResult();

   ModifiableItemList createItemList();

   ModifiableValueList createValueList();

   ModifiableField createField(String var1);

   ModifiableField createField(String var1, String var2);
}
