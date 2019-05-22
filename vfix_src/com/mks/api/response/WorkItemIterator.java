package com.mks.api.response;

public interface WorkItemIterator {
   WorkItem next() throws APIException;

   boolean hasNext();

   WorkItem getLast();
}
