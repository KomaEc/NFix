package com.mks.api.response.impl;

import com.mks.api.response.APIException;
import com.mks.api.response.Result;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.SubRoutineIterator;
import com.mks.api.response.modifiable.ModifiableWorkItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkItemImpl extends ItemImpl implements ModifiableWorkItem {
   private Result result;
   private APIException apiException;
   private List subRoutineList = new ArrayList();

   protected WorkItemImpl(String id, String context, String modelType) {
      super(id, context, modelType);
   }

   public APIException getAPIException() {
      return this.apiException;
   }

   public void setAPIException(APIException ex) {
      this.apiException = ex;
   }

   public Result getResult() {
      return this.result;
   }

   public void setResult(Result result) {
      this.result = result;
   }

   public SubRoutineIterator getSubRoutines() {
      return new SubRoutineIteratorImpl(this.subRoutineList.iterator());
   }

   public List getSubRoutineList() {
      return this.subRoutineList;
   }

   public int getSubRoutineListSize() {
      return this.subRoutineList.size();
   }

   public SubRoutine getSubRoutine(String name) {
      Iterator it = this.subRoutineList.iterator();

      SubRoutine sr;
      do {
         if (!it.hasNext()) {
            return null;
         }

         sr = (SubRoutine)it.next();
      } while(!sr.getRoutine().equals(name));

      return sr;
   }

   public boolean containsSubRoutine(String name) {
      Iterator it = this.subRoutineList.iterator();

      SubRoutine sr;
      do {
         if (!it.hasNext()) {
            return false;
         }

         sr = (SubRoutine)it.next();
      } while(!sr.getRoutine().equals(name));

      return true;
   }

   public void add(SubRoutine subRoutine) {
      this.subRoutineList.add(subRoutine);
   }
}
