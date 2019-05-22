package com.mks.api.response.impl;

import com.mks.api.IntegrationPointFactory;
import com.mks.api.response.APIException;
import com.mks.api.response.APIInternalError;
import com.mks.api.response.Field;
import com.mks.api.response.FieldContainer;
import com.mks.api.response.InterruptedException;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.SubRoutineIterator;
import com.mks.api.response.ValueList;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemContainer;
import com.mks.api.response.WorkItemIterator;
import com.mks.api.util.EscapedStringTokenizer;
import com.mks.api.util.MKSLogger;
import com.mks.api.util.ResponseUtil;
import java.io.PrintStream;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ResponseWalker {
   private Response response;
   private Object current;
   private List parents;
   private Object value;
   private static final char LIST_DELIMITER = '\u0001';
   private static final char RECORD_DELIMITER = '\u0002';
   private SubRoutine subRoutine;
   private WorkItem workItem;
   private Item item;
   private ItemList itemList;
   private Field field;
   private Result result;
   private APIException exception;
   private ValueList list;
   private Object currentWorkingObject;
   private int srIdx;
   private int wiIdx;
   private int ilIdx;
   private int iIdx;
   private int fIdx;
   private int lIdx;
   public static final String DIR_DELIM = ";";
   public static final String VAL_DELIM = "=";
   public static final String FIRST = "first";
   public static final String NEXT = "next";
   public static final String LAST = "last";
   public static final String PARENT = "parent";
   public static final String RESPONSE = "response";
   public static final String WORK_ITEM = "workitem";
   public static final String SUB_ROUTINE = "subroutine";
   public static final String ITEM_LIST = "itemlist";
   public static final String ITEM = "item";
   public static final String FIELD = "field";
   public static final String LIST = "list";
   public static final String RESULT = "result";
   public static final String EXCEPTION = "exception";
   public static final String PRINT = "print";
   private MKSLogger apiLogger;

   public ResponseWalker(Response response) {
      this.response = response;
      this.srIdx = -1;
      this.wiIdx = -1;
      this.ilIdx = -1;
      this.iIdx = -1;
      this.fIdx = -1;
      this.lIdx = -1;
      this.parents = new ArrayList();
      this.current = this.response;
      this.apiLogger = IntegrationPointFactory.getLogger();
      this.currentWorkingObject = this.response;
   }

   private ResponseWalker.CurrentState captureStartState() {
      ResponseWalker.CurrentState cs = new ResponseWalker.CurrentState(this.current, this.currentWorkingObject, this.parents);
      cs.srIdx = this.srIdx;
      cs.wiIdx = this.wiIdx;
      cs.ilIdx = this.ilIdx;
      cs.iIdx = this.iIdx;
      cs.fIdx = this.fIdx;
      cs.lIdx = this.lIdx;
      return cs;
   }

   private void restoreStartState(ResponseWalker.CurrentState currentState) {
      this.current = currentState.currentPtr;
      this.parents = new ArrayList(currentState.parents);
      this.srIdx = currentState.srIdx;
      this.wiIdx = currentState.wiIdx;
      this.ilIdx = currentState.ilIdx;
      this.iIdx = currentState.iIdx;
      this.fIdx = currentState.fIdx;
      this.lIdx = currentState.lIdx;
      this.currentWorkingObject = currentState.currentWorkingPtr;
   }

   public void walk(String directives) throws CommandException {
      EscapedStringTokenizer st = new EscapedStringTokenizer(directives, ";", false);
      ResponseWalker.CurrentState cs = this.captureStartState();
      String token = null;
      this.apiLogger.message((Object)this, "API", 10, "Directives: " + directives);

      String targetType;
      try {
         while(st.hasMoreTokens()) {
            token = st.nextToken();
            if (token.equals("response")) {
               this.apiLogger.message((Object)this, "API", 10, "Resetting ResponseWalker pointers.");
               this.parents.clear();
               this.current = this.response;
            } else {
               EscapedStringTokenizer ist = new EscapedStringTokenizer(token, "=", false);
               targetType = ist.nextToken();
               String targetId = ist.hasMoreTokens() ? ist.nextToken() : "";
               this.apiLogger.message((Object)this, "API", 10, "Directive: " + token);
               if (targetType.equals("print")) {
                  this.printCurrentNode();
               } else if (targetType.equals("parent")) {
                  this.setCurrent(this.parents.remove(this.parents.size() - 1));
               } else {
                  this.checkPointers(targetType);
                  if (this.current == this.subRoutine) {
                     this.walkSubRoutine(targetType, targetId);
                  } else if (this.current == this.workItem) {
                     this.walkWorkItem(targetType, targetId);
                  } else if (this.current == this.item) {
                     this.walkItem(targetType, targetId);
                  } else if (this.current == this.itemList) {
                     this.walkItemList(targetType, targetId);
                  } else if (this.current == this.list) {
                     this.walkList(targetType, targetId);
                  } else if (this.current == this.result) {
                     this.walkResult(targetType, targetId);
                  } else if (this.current == this.exception) {
                     this.walkException(targetType, targetId);
                  } else if (this.current == this.field) {
                     this.walkField(targetType, targetId);
                  } else if (this.current == this.response) {
                     this.walkResponse(targetType, targetId);
                  }
               }
            }
         }

      } catch (CommandException var8) {
         this.restoreStartState(cs);
         this.apiLogger.exception((Object)this, "API", 0, var8);
         throw var8;
      } catch (NoSuchElementException var9) {
         this.restoreStartState(cs);
         targetType = "Invalid directive: " + token;
         this.apiLogger.message((Object)this, "API", 0, targetType);
         throw new InvalidDirectiveException(targetType);
      } catch (IndexOutOfBoundsException var10) {
         this.restoreStartState(cs);
         this.apiLogger.exception((Object)this, "API", 0, var10);
         throw new InvalidDirectiveException(var10.getMessage());
      } catch (Throwable var11) {
         this.restoreStartState(cs);
         this.apiLogger.exception((Object)this, "API", 0, var11);
         throw new CommandException(var11);
      }
   }

   public String getValue() throws CommandException {
      if (this.value != null) {
         if (this.value instanceof Field) {
            return this.getFieldValue((Field)this.value);
         } else {
            return this.value instanceof Item ? ((Item)this.value).getId() : this.value.toString();
         }
      } else {
         String msg = "Invalid node to retrieve a value from.";
         throw new InvalidValueException(msg);
      }
   }

   public Object getCurrentObject() throws CommandException {
      if (this.currentWorkingObject != null) {
         return this.currentWorkingObject;
      } else {
         String msg = "Invalid node to retrieve a value from.";
         throw new InvalidValueException(msg);
      }
   }

   private String getFieldValue(Field field) throws CommandException {
      Object v = field.getValue();
      if (!(v instanceof ItemList) && !(v instanceof ValueList)) {
         if (v != null) {
            if (v instanceof Date) {
               SimpleDateFormat sdf = new SimpleDateFormat();
               StringBuffer sb = new StringBuffer(27);
               sdf.applyPattern("yyyy-MM-dd-HH.mm.ss.SSSSSS");
               v = sdf.format((Date)v, sb, new FieldPosition(0));
            } else if (v instanceof Item) {
               return ((Item)v).getId();
            }

            return v.toString();
         } else {
            String msg = "Field value: <null>";
            throw new InvalidValueException(msg);
         }
      } else {
         StringBuffer sb = new StringBuffer(1024);
         Iterator it = ((List)v).iterator();
         Object o;
         if (it.hasNext()) {
            o = it.next();
            if (o instanceof Item) {
               sb.append(((Item)o).getId());
            } else {
               sb.append(o);
            }
         }

         while(it.hasNext()) {
            sb.append('\u0001');
            o = it.next();
            if (o instanceof Item) {
               sb.append(((Item)o).getId());
            } else {
               sb.append(o);
            }
         }

         return sb.toString();
      }
   }

   public String getRecordValue() throws CommandException {
      if (this.currentWorkingObject == null) {
         String msg = "Response object model pointer not set!";
         throw new InvalidValueException(msg);
      } else {
         try {
            StringBuffer sb = new StringBuffer(1024);
            if (this.currentWorkingObject instanceof WorkItemContainer) {
               WorkItemIterator wii = ((WorkItemContainer)this.currentWorkingObject).getWorkItems();
               WorkItem wi = null;
               if (wii.hasNext()) {
                  try {
                     wi = wii.next();
                  } catch (APIException var8) {
                     wi = wii.getLast();
                  }

                  sb.append(wi.getId());
               }

               while(wii.hasNext()) {
                  try {
                     wi = wii.next();
                  } catch (APIException var7) {
                     wi = wii.getLast();
                  }

                  sb.append('\u0002');
                  sb.append(wi.getId());
               }
            } else {
               if (!(this.currentWorkingObject instanceof FieldContainer)) {
                  String msg = "Cannot return record.  Invalid target node: " + this.currentWorkingObject;
                  throw new InvalidValueException(msg);
               }

               Iterator it = ((FieldContainer)this.currentWorkingObject).getFields();
               if (it.hasNext()) {
                  try {
                     sb.append(this.getFieldValue((Field)it.next()));
                  } catch (InvalidValueException var6) {
                  }
               }

               while(it.hasNext()) {
                  sb.append('\u0002');

                  try {
                     sb.append(this.getFieldValue((Field)it.next()));
                  } catch (InvalidValueException var5) {
                  }
               }
            }

            return sb.toString();
         } catch (APIInternalError var9) {
            this.apiLogger.exception((Object)this, "API", 0, var9);
            throw new InvalidValueException(var9);
         }
      }
   }

   private void walkResponse(String target, String id) throws CommandException {
      try {
         if (!this.isResultOrException("response", target, id)) {
            boolean lookupById = this.lookupById(target, id);
            String msg;
            int i;
            if (target.equals("subroutine")) {
               msg = "Walking Response->SubRoutine(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               if (lookupById) {
                  this.subRoutine = this.response.getSubRoutine(id);
                  this.current = this.subRoutine;
                  this.currentWorkingObject = this.subRoutine;
               } else {
                  if (this.srIdx == Integer.MAX_VALUE) {
                     this.srIdx = this.response.getSubRoutineListSize() - 1;
                  }

                  this.parents.add(this.response);
                  this.current = this.response;
                  SubRoutineIterator it = this.response.getSubRoutines();

                  for(i = 0; i < this.srIdx; ++i) {
                     this.subRoutine = it.next();
                  }

                  this.currentWorkingObject = this.subRoutine;
               }
            } else {
               if (!target.equals("workitem")) {
                  msg = "Invalid directive: Response->" + target;
                  this.apiLogger.message((Object)this, "API", 0, msg);
                  throw new InvalidDirectiveException(msg);
               }

               msg = "Walking Response->WorkItem(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               if (lookupById) {
                  this.workItem = this.response.getWorkItem(id);
                  this.current = this.workItem;
                  this.currentWorkingObject = this.workItem;
               } else {
                  if (this.wiIdx == Integer.MAX_VALUE) {
                     this.wiIdx = this.response.getWorkItemListSize() - 1;
                  }

                  this.parents.add(this.response);
                  this.current = this.response;
                  WorkItemIterator it = this.response.getWorkItems();

                  for(i = 0; i < this.wiIdx; ++i) {
                     this.workItem = it.next();
                  }

                  this.currentWorkingObject = this.workItem;
               }

               this.value = this.workItem;
            }

         }
      } catch (APIInternalError var7) {
         this.apiLogger.exception((Object)this, "API", 0, var7);
         throw new CommandException(var7);
      } catch (APIException var8) {
         this.apiLogger.exception((Object)this, "API", 0, var8);
         throw new CommandException(var8);
      }
   }

   private void walkSubRoutine(String target, String id) throws CommandException {
      try {
         if (!this.isResultOrException("subroutine", target, id)) {
            boolean lookupById = this.lookupById(target, id);
            String msg;
            int i;
            if (target.equals("subroutine")) {
               msg = "Walking SubRoutine->SubRoutine(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               if (lookupById) {
                  this.parents.add(this.subRoutine);
                  this.subRoutine = this.subRoutine.getSubRoutine(id);
                  this.current = this.subRoutine;
                  this.currentWorkingObject = this.subRoutine;
               } else {
                  if (this.srIdx == Integer.MAX_VALUE) {
                     this.srIdx = this.subRoutine.getSubRoutineListSize() - 1;
                  }

                  this.parents.add(this.subRoutine);
                  this.current = this.subRoutine;
                  SubRoutineIterator it = this.subRoutine.getSubRoutines();

                  for(i = 0; i < this.srIdx; ++i) {
                     this.subRoutine = it.next();
                  }

                  this.currentWorkingObject = this.subRoutine;
               }
            } else {
               if (!target.equals("workitem")) {
                  msg = "Invalid directive: SubRoutine->" + target;
                  this.apiLogger.message((Object)this, "API", 0, msg);
                  throw new InvalidDirectiveException(msg);
               }

               msg = "Walking SubRoutine->WorkItem(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               if (lookupById) {
                  this.parents.add(this.subRoutine);
                  this.workItem = this.subRoutine.getWorkItem(id);
                  this.current = this.workItem;
                  this.currentWorkingObject = this.workItem;
               } else {
                  if (this.wiIdx == Integer.MAX_VALUE) {
                     this.wiIdx = this.subRoutine.getWorkItemListSize() - 1;
                  }

                  this.parents.add(this.subRoutine);
                  this.current = this.subRoutine;
                  WorkItemIterator it = this.subRoutine.getWorkItems();

                  for(i = 0; i < this.wiIdx; ++i) {
                     this.workItem = it.next();
                  }

                  this.currentWorkingObject = this.workItem;
               }

               this.value = this.workItem;
            }

         }
      } catch (APIInternalError var7) {
         this.apiLogger.exception((Object)this, "API", 0, var7);
         throw new CommandException(var7);
      } catch (APIException var8) {
         this.apiLogger.exception((Object)this, "API", 0, var8);
         throw new CommandException(var8);
      }
   }

   private void walkWorkItem(String target, String id) throws CommandException {
      try {
         if (!this.isResultOrException("workitem", target, id)) {
            boolean lookupById = this.lookupById(target, id);
            String msg;
            int i;
            if (target.equals("subroutine")) {
               msg = "Walking WorkItem->SubRoutine(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               if (lookupById) {
                  this.parents.add(this.workItem);
                  this.subRoutine = this.workItem.getSubRoutine(id);
                  this.current = this.subRoutine;
                  this.currentWorkingObject = this.subRoutine;
               } else {
                  if (this.srIdx == Integer.MAX_VALUE) {
                     this.srIdx = this.workItem.getSubRoutineListSize() - 1;
                  }

                  this.parents.add(this.workItem);
                  this.current = this.workItem;
                  SubRoutineIterator it = this.workItem.getSubRoutines();

                  for(i = 0; i < this.srIdx; ++i) {
                     this.subRoutine = it.next();
                  }

                  this.currentWorkingObject = this.subRoutine;
               }
            } else {
               if (!target.equals("field")) {
                  msg = "Invalid directive: WorkItem->" + target;
                  this.apiLogger.message((Object)this, "API", 0, msg);
                  throw new InvalidDirectiveException(msg);
               }

               msg = "Walking WorkItem->Field(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               if (lookupById) {
                  this.parents.add(this.workItem);
                  this.field = this.workItem.getField(id);
                  this.current = this.field;
                  this.currentWorkingObject = this.field;
               } else {
                  if (this.fIdx == Integer.MAX_VALUE) {
                     this.fIdx = this.workItem.getFieldListSize() - 1;
                  }

                  this.parents.add(this.workItem);
                  this.current = this.workItem;
                  Iterator it = this.workItem.getFields();

                  for(i = 0; i < this.fIdx; ++i) {
                     this.field = (Field)it.next();
                  }

                  this.currentWorkingObject = this.field;
               }

               this.value = this.field;
            }

         }
      } catch (APIInternalError var7) {
         this.apiLogger.exception((Object)this, "API", 0, var7);
         throw new CommandException(var7);
      } catch (APIException var8) {
         this.apiLogger.exception((Object)this, "API", 0, var8);
         throw new CommandException(var8);
      }
   }

   private void walkItemList(String target, String id) throws CommandException {
      boolean lookupById = this.lookupById(target, id);
      String msg;
      if (!target.equals("item")) {
         msg = "Invalid directive: ItemList->" + target;
         this.apiLogger.message((Object)this, "API", 0, msg);
         throw new InvalidDirectiveException(msg);
      } else {
         msg = "Walking ItemList->Item(" + id + ")";
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (lookupById) {
            this.parents.add(this.itemList);
            this.item = this.itemList.getItem(id);
            this.current = this.item;
            this.currentWorkingObject = this.item;
         } else {
            if (this.iIdx == Integer.MAX_VALUE) {
               this.iIdx = this.itemList.getItemListSize() - 1;
            }

            this.parents.add(this.itemList);
            Iterator it = this.itemList.getItems();

            for(int i = 0; i < this.iIdx; ++i) {
               this.item = (Item)it.next();
            }

            this.current = this.itemList;
            this.currentWorkingObject = this.item;
         }

         this.value = this.item;
      }
   }

   private void walkList(String target, String id) throws CommandException {
      String msg;
      if (this.lookupById(target, id)) {
         msg = "Invalid directive: List->" + target;
         this.apiLogger.message((Object)this, "API", 0, msg);
         throw new InvalidDirectiveException(msg);
      } else if (target.equals("list")) {
         msg = "Walking List(" + id + ")";
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (this.lIdx == Integer.MAX_VALUE) {
            this.lIdx = this.list.size() - 1;
         }

         this.value = this.list.get(this.lIdx);
         this.currentWorkingObject = this.value;
      } else {
         msg = "Invalid directive: List->" + target;
         this.apiLogger.message((Object)this, "API", 0, msg);
         throw new InvalidDirectiveException(msg);
      }
   }

   private void walkItem(String target, String id) throws CommandException {
      boolean lookupById = this.lookupById(target, id);
      String msg;
      if (!target.equals("field")) {
         msg = "Invalid directive: Item->" + target;
         this.apiLogger.message((Object)this, "API", 0, msg);
         throw new InvalidDirectiveException(msg);
      } else {
         msg = "Walking Item->Field(" + id + ")";
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (lookupById) {
            this.parents.add(this.item);
            this.field = this.item.getField(id);
         } else {
            if (this.fIdx == Integer.MAX_VALUE) {
               this.fIdx = this.item.getFieldListSize() - 1;
            }

            this.parents.add(this.item);
            Iterator it = this.item.getFields();

            for(int i = 0; i < this.fIdx; ++i) {
               this.field = (Field)it.next();
            }
         }

         this.current = this.field;
         this.value = this.field;
         this.currentWorkingObject = this.value;
      }
   }

   private void walkField(String target, String id) throws CommandException {
      String msg;
      if (!target.equals("list") && !target.equals("itemlist")) {
         if (!target.equals("item")) {
            msg = "Invalid directive: Field->" + target;
            this.apiLogger.message((Object)this, "API", 0, msg);
            throw new InvalidDirectiveException(msg);
         }

         msg = "Walking Field->Item(" + id + ")";
         this.apiLogger.message((Object)this, "API", 10, msg);
         this.parents.add(this.field);
         this.item = this.field.getItem();
         this.current = this.item;
         this.currentWorkingObject = this.value;
      } else {
         msg = "Walking Field->List(" + id + ")";
         this.apiLogger.message((Object)this, "API", 10, msg);
         this.parents.add(this.field);
         List l = this.field.getList();
         if (l instanceof ItemList) {
            this.itemList = (ItemList)l;
            this.current = this.itemList;
            this.currentWorkingObject = this.itemList;
         } else {
            this.list = (ValueList)l;
            this.current = this.list;
            this.currentWorkingObject = this.list;
         }
      }

   }

   private void walkResult(String target, String id) throws CommandException {
      boolean lookupById = this.lookupById(target, id);
      String msg;
      if (!target.equals("field")) {
         msg = "Invalid directive: Result->" + target;
         this.apiLogger.message((Object)this, "API", 0, msg);
         throw new InvalidDirectiveException(msg);
      } else {
         msg = "Walking Result->Field(" + id + ")";
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (lookupById) {
            this.parents.add(this.result);
            this.field = this.result.getField(id);
         } else {
            if (this.fIdx == Integer.MAX_VALUE) {
               this.fIdx = this.result.getFieldListSize() - 1;
            }

            this.parents.add(this.result);
            Iterator it = this.result.getFields();

            for(int i = 0; i < this.fIdx; ++i) {
               this.field = (Field)it.next();
            }
         }

         this.current = this.field;
         this.value = this.field;
         this.currentWorkingObject = this.field;
      }
   }

   private void walkException(String target, String id) throws CommandException {
      boolean lookupById = this.lookupById(target, id);
      String msg;
      if (!target.equals("field")) {
         msg = "Invalid directive: APIException->" + target;
         this.apiLogger.message((Object)this, "API", 0, msg);
         throw new InvalidDirectiveException(msg);
      } else {
         msg = "Walking Exception->Field(" + id + ")";
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (lookupById) {
            this.parents.add(this.exception);
            this.field = this.exception.getField(id);
         } else {
            if (this.fIdx == Integer.MAX_VALUE) {
               this.fIdx = this.exception.getFieldListSize() - 1;
            }

            this.parents.add(this.result);
            Iterator it = this.exception.getFields();

            for(int i = 0; i < this.fIdx; ++i) {
               this.field = (Field)it.next();
            }
         }

         this.current = this.field;
         this.value = this.field;
         this.currentWorkingObject = this.field;
      }
   }

   private boolean isResultOrException(String source, String target, String id) throws CommandException {
      try {
         String msg;
         if (target.equals("result")) {
            if (source.equals("response")) {
               msg = "Walking Response->Result(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               this.parents.add(this.response);
               this.result = this.response.getResult();
            } else if (source.equals("subroutine")) {
               msg = "Walking SubRoutine->Result(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               this.parents.add(this.subRoutine);
               this.result = this.subRoutine.getResult();
            } else if (source.equals("workitem")) {
               msg = "Walking WorkItem->Result(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               this.parents.add(this.workItem);
               this.result = this.workItem.getResult();
            }

            if (this.result != null) {
               this.currentWorkingObject = this.result;
               return true;
            }
         } else if (target.equals("exception")) {
            if (source.equals("response")) {
               msg = "Walking Response->Exception(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               this.parents.add(this.response);
               this.exception = this.response.getAPIException();
            } else if (source.equals("subroutine")) {
               msg = "Walking SubRoutine->Exception(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               this.parents.add(this.subRoutine);
               this.exception = this.subRoutine.getAPIException();
            } else if (source.equals("workitem")) {
               msg = "Walking WorkItem->Exception(" + id + ")";
               this.apiLogger.message((Object)this, "API", 10, msg);
               this.parents.add(this.workItem);
               this.exception = this.workItem.getAPIException();
            }

            if (this.exception != null) {
               this.currentWorkingObject = this.exception;
               return true;
            }
         } else if (id == null) {
            msg = "Invalid directive: " + target + "=null";
            this.apiLogger.message((Object)this, "API", 0, msg);
            throw new InvalidDirectiveException(msg);
         }

         return false;
      } catch (InterruptedException var5) {
         this.apiLogger.exception((Object)this, "API", 0, var5);
         throw new CommandException(var5);
      } catch (APIInternalError var6) {
         this.apiLogger.exception((Object)this, "API", 0, var6);
         throw new CommandException(var6);
      }
   }

   private boolean lookupById(String target, String id) {
      String msg;
      if (id.equals("first")) {
         msg = "Getting first " + target;
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (target.equals("subroutine")) {
            this.srIdx = 1;
         } else if (target.equals("workitem")) {
            this.wiIdx = 1;
         } else if (target.equals("itemlist")) {
            this.ilIdx = 1;
         } else if (target.equals("item")) {
            this.iIdx = 1;
         } else if (target.equals("field")) {
            this.fIdx = 1;
         } else if (target.equals("list")) {
            this.lIdx = 1;
         }
      } else if (id.equals("last")) {
         msg = "Getting last " + target;
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (target.equals("subroutine")) {
            this.srIdx = Integer.MAX_VALUE;
         } else if (target.equals("workitem")) {
            this.wiIdx = Integer.MAX_VALUE;
         } else if (target.equals("itemlist")) {
            this.ilIdx = Integer.MAX_VALUE;
         } else if (target.equals("item")) {
            this.iIdx = Integer.MAX_VALUE;
         } else if (target.equals("field")) {
            this.fIdx = Integer.MAX_VALUE;
         } else if (target.equals("list")) {
            this.lIdx = Integer.MAX_VALUE;
         }
      } else {
         if (!id.equals("next")) {
            return true;
         }

         msg = "Getting next " + target;
         this.apiLogger.message((Object)this, "API", 10, msg);
         if (target.equals("subroutine")) {
            ++this.srIdx;
         } else if (target.equals("workitem")) {
            ++this.wiIdx;
         } else if (target.equals("itemlist")) {
            ++this.ilIdx;
         } else if (target.equals("item")) {
            ++this.iIdx;
         } else if (target.equals("field")) {
            ++this.fIdx;
         } else if (target.equals("list")) {
            ++this.lIdx;
         }
      }

      return false;
   }

   private void setCurrent(Object parent) {
      if (parent instanceof Response) {
         this.response = (Response)parent;
         this.current = this.response;
         this.currentWorkingObject = this.current;
         this.apiLogger.message((Object)this, "API", 10, "Setting current pointer to Response");
      } else if (parent instanceof SubRoutine) {
         this.subRoutine = (SubRoutine)parent;
         this.current = this.subRoutine;
         this.currentWorkingObject = this.current;
         this.apiLogger.message((Object)this, "API", 10, "Setting current pointer to SubRoutine");
      } else if (parent instanceof WorkItem) {
         this.workItem = (WorkItem)parent;
         this.current = this.workItem;
         this.currentWorkingObject = this.current;
         this.apiLogger.message((Object)this, "API", 10, "Setting current pointer to WorkItem");
      } else if (parent instanceof ItemList) {
         this.itemList = (ItemList)parent;
         this.current = this.itemList;
         this.currentWorkingObject = this.current;
         this.apiLogger.message((Object)this, "API", 10, "Setting current pointer to ItemList");
      } else if (parent instanceof Item) {
         this.item = (Item)parent;
         this.current = this.item;
         this.currentWorkingObject = this.current;
         this.apiLogger.message((Object)this, "API", 10, "Setting current pointer to Item");
      } else if (parent instanceof ValueList) {
         this.list = (ValueList)parent;
         this.current = this.list;
         this.currentWorkingObject = this.current;
         this.apiLogger.message((Object)this, "API", 10, "Setting current pointer to List");
      } else if (parent instanceof Field) {
         this.field = (Field)parent;
         this.current = this.field;
         this.currentWorkingObject = this.current;
         this.apiLogger.message((Object)this, "API", 10, "Setting current pointer to Field");
      }

   }

   private void checkPointers(String targetType) {
      Object saved = this.current;
      if (targetType.equals("item")) {
         if (this.current != this.workItem && this.current != this.item) {
            if (this.current == this.field && this.itemList != null) {
               this.current = this.itemList;
            }
         } else {
            this.current = this.field;
         }
      } else if (targetType.equals("list")) {
         if (this.current != this.field) {
            this.current = this.field;
         }
      } else if (targetType.equals("itemlist")) {
         if (this.current != this.field && this.current != this.workItem) {
            if (this.workItem != null) {
               this.current = this.workItem;
            } else {
               this.current = this.field;
            }
         }
      } else if (targetType.equals("field")) {
         if (this.current != this.response && this.current != this.subRoutine) {
            if (this.current == this.itemList) {
               this.current = this.item;
            }
         } else if (this.workItem != null) {
            this.current = this.workItem;
         } else {
            this.current = this.result;
         }
      }

      if (this.current == null) {
         this.apiLogger.message((Object)this, "API", 10, "Tried to reset the current pointer to null for target " + targetType);
         this.current = saved;
      }

   }

   private void printCurrentNode() {
      if (this.current == this.subRoutine) {
         ResponseUtil.printSubRoutine(this.subRoutine, 1, (PrintStream)System.out);
      } else if (this.current == this.workItem) {
         ResponseUtil.printWorkItem(this.workItem, 1, (PrintStream)System.out);
      } else if (this.current == this.item) {
         ResponseUtil.printItem(this.item, 1, (PrintStream)System.out);
      } else if (this.current == this.itemList) {
         ResponseUtil.printItemList(this.itemList, 1, (PrintStream)System.out);
      } else if (this.current == this.list) {
         ResponseUtil.printList(this.list, 1, (PrintStream)System.out);
      } else if (this.current == this.result) {
         ResponseUtil.printResult(this.result, 1, (PrintStream)System.out);
      } else if (this.current == this.exception) {
         ResponseUtil.printAPIException(this.exception, 1, (PrintStream)System.out);
      } else if (this.current == this.field) {
         ResponseUtil.printField(this.field, 1, (PrintStream)System.out);
      } else if (this.current == this.response) {
         ResponseUtil.printResponse(this.response, 1, (PrintStream)System.out, true);
      }

   }

   private class CurrentState {
      public int srIdx;
      public int wiIdx;
      public int ilIdx;
      public int iIdx;
      public int fIdx;
      public int lIdx;
      public Object currentPtr;
      public Object currentWorkingPtr;
      public List parents;

      public CurrentState(Object cp, Object cwp, List p) {
         this.currentPtr = cp;
         this.currentWorkingPtr = cwp;
         this.parents = new ArrayList(p);
      }
   }
}
