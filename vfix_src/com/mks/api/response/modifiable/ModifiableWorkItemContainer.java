package com.mks.api.response.modifiable;

import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemContainer;

public interface ModifiableWorkItemContainer extends WorkItemContainer {
   void add(WorkItem var1);
}
