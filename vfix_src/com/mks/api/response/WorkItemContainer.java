package com.mks.api.response;

public interface WorkItemContainer {
   int getWorkItemListSize();

   WorkItemIterator getWorkItems();

   WorkItem getWorkItem(String var1);

   WorkItem getWorkItem(String var1, String var2);

   boolean containsWorkItem(String var1);

   boolean containsWorkItem(String var1, String var2);
}
