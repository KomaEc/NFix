package com.mks.api.commands.ide;

import com.mks.api.response.APIError;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.InvalidItemException;
import com.mks.api.response.Item;
import com.mks.api.response.ItemNotFoundException;
import com.mks.api.response.WorkItem;
import com.mks.api.response.impl.SimpleResponseFactory;
import com.mks.api.response.modifiable.ModifiableField;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class WorkingFile implements Item {
   public static final String INVALID_MOVE_TO_CP_OPERATION = "INVALID_MOVE_TO_CP_OPERATION";
   private long ordinal;
   private boolean valid;
   private static final String WF_FIELD_EXCLUSIVE_LOCKED_BY_OTHER = "exclusiveLockedByOther";
   private static final String WF_FIELD_LOCKED_BY_OTHER = "lockedByOther";
   private static final String WF_FIELD_LOCKED_BY_ME = "lockedByMe";
   private static final String WF_FIELD_OUT_OF_DATE = "outOfDate";
   private static final String WF_FIELD_MODIFIED = "modified";
   private static final String WF_FIELD_INCOMING = "incoming";
   private static final String WF_FIELD_MOVED = "moved";
   private static final String WF_FIELD_DROPPED = "dropped";
   private static final String WF_FIELD_ADDED = "added";
   private static final String WF_FIELD_FORMER_MEMBER = "formerMember";
   private static final String WF_FIELD_MEMBER = "member";
   private static final String WF_FIELD_CONTROLLED = "controlled";
   private static final String WF_FIELD_IN_SANDBOX_DIR = "inSandboxDir";
   private static final String WF_FIELD_WORKING_DELTA = "workingDelta";
   private static final String WF_FIELD_WORKING_CPID = "workingCpid";
   private static final String WF_FIELD_WORKING_REV = "workingRev";
   private static final String WF_FIELD_MEMBER_REV = "memberRev";
   private static final String WF_FIELD_SANDBOX = "sandbox";
   private static final String WF_FIELD_IS_SHARED = "shared";
   private static final String WF_FIELD_MEMBER_NAME = "memberName";
   private static final String WF_FIELD_IS_PENDING = "pending";
   private static final String WF_FIELD_IS_PENDING_ADD = "pendingAdd";
   private static final String WF_FIELD_EXCLUSIVE = "exclusiveLockedByMe";
   private static final String WF_FIELD_SUBMITTABLE = "submittable";
   private static final String WF_FIELD_INVALID = "invalid";
   private static final String WF_FIELD_ORDINAL = "ordinal";
   private Map fields;
   private File file;
   private File workingRoot;
   private APIException exception;

   WorkingFile(File file, String sandbox, File root, WorkItem viewsandboxWorkItem, Date timestamp, String userId, long ordinal) {
      this(file, sandbox, root, false, timestamp, ordinal);
      this.generateFromWorkItem(viewsandboxWorkItem, userId);
   }

   WorkingFile(File file, APIException ex, Date timestamp, long ordinal) {
      this(file, (String)null, (File)null, false, ex, timestamp, ordinal);
   }

   WorkingFile(File file, String sandbox, File root, boolean controlled, Date timestamp, long ordinal) {
      this(file, sandbox, root, controlled, (APIException)null, timestamp, ordinal);
   }

   private WorkingFile(File file, String sandbox, File root, boolean controlled, APIException ex, Date timestamp, long ordinal) {
      this.fields = new HashMap(24);
      this.fields.put("memberName", (Object)null);
      this.fields.put("sandbox", (Object)null);
      this.fields.put("inSandboxDir", Boolean.FALSE);
      this.fields.put("controlled", Boolean.FALSE);
      this.fields.put("member", Boolean.FALSE);
      this.fields.put("formerMember", Boolean.FALSE);
      this.fields.put("added", Boolean.FALSE);
      this.fields.put("dropped", Boolean.FALSE);
      this.fields.put("moved", Boolean.FALSE);
      this.fields.put("modified", Boolean.FALSE);
      this.fields.put("outOfDate", Boolean.FALSE);
      this.fields.put("incoming", Boolean.FALSE);
      this.fields.put("lockedByMe", Boolean.FALSE);
      this.fields.put("lockedByOther", Boolean.FALSE);
      this.fields.put("exclusiveLockedByOther", Boolean.FALSE);
      this.fields.put("pending", Boolean.FALSE);
      this.fields.put("pendingAdd", Boolean.FALSE);
      this.fields.put("exclusiveLockedByMe", Boolean.FALSE);
      this.fields.put("workingDelta", Boolean.FALSE);
      this.fields.put("workingCpid", (Object)null);
      this.fields.put("workingRev", (Object)null);
      this.fields.put("memberRev", (Object)null);
      this.fields.put("shared", Boolean.FALSE);
      this.exception = null;
      if (timestamp == null) {
         throw new APIError("Assertion: Working File missing timestamp");
      } else {
         this.file = file;
         this.workingRoot = root;
         this.ordinal = ordinal;
         this.fields.put("memberName", file);
         this.fields.put("sandbox", sandbox);
         this.fields.put("inSandboxDir", sandbox == null ? Boolean.FALSE : Boolean.TRUE);
         this.fields.put("controlled", controlled ? Boolean.TRUE : Boolean.FALSE);
         if (ex == null) {
            if (file == null || file.isDirectory()) {
               ex = new InvalidItemException();
            }

            if (!file.exists()) {
               ex = new ItemNotFoundException();
            }
         }

         this.setAPIException((APIException)ex);
      }
   }

   private File constructMemberFile(String sandbox, String relativePath) {
      File sandboxDir = (new File(sandbox)).getParentFile();
      return new File(sandboxDir, relativePath);
   }

   private void generateFromWorkItem(WorkItem wi, String userId) {
      APIException apix = wi.getAPIException();
      String exceptionName;
      if (apix != null) {
         exceptionName = apix.getField("exception-name").getString();
         if (apix instanceof ItemNotFoundException) {
            if (!"si.MemberNotFound".equals(exceptionName)) {
               if ("si.NoSuchSubproject".equals(exceptionName)) {
                  this.fields.put("formerMember", Boolean.TRUE);
               } else {
                  this.setAPIException(apix);
               }
            }

         } else {
            this.setAPIException(apix);
         }
      } else {
         if (this.getAPIException() instanceof ItemNotFoundException) {
            this.setAPIException((APIException)null);
         }

         this.fields.put("controlled", Boolean.TRUE);
         this.fields.put("inSandboxDir", Boolean.TRUE);
         if (!"si.Sandbox".equals(wi.getModelType()) && !"si.Project".equals(wi.getModelType())) {
            exceptionName = wi.getField("name").getString();
            this.file = new File(exceptionName);
            String sandbox = wi.getField("canonicalSandbox").getString();
            String relativeMemberName = wi.getField("canonicalMember").getString();
            File memberFile = this.constructMemberFile(sandbox, relativeMemberName);
            this.fields.put("memberName", memberFile);
            this.fields.put("sandbox", sandbox);
            if ("si.FormerMember".equals(wi.getModelType())) {
               this.fields.put("formerMember", Boolean.TRUE);
            } else {
               String workingCpid = null;

               Item item;
               try {
                  item = wi.getField("workingcpid").getItem();
                  if (item != null) {
                     workingCpid = item.getId();
                  }
               } catch (NoSuchElementException var25) {
               }

               this.fields.put("workingCpid", workingCpid);
               if (!this.file.equals(memberFile)) {
                  this.fields.put("moved", Boolean.TRUE);
               }

               String type = wi.getField("type").getString();
               if ("si.DestinedMember".equals(wi.getModelType())) {
                  if ("deferred-add".equals(type)) {
                     this.fields.put("added", Boolean.TRUE);
                     return;
                  }

                  if ("deferred-move-to".equals(type)) {
                     return;
                  }
               }

               if (type.indexOf("pending-add") != -1) {
                  this.fields.put("pendingAdd", Boolean.TRUE);
               }

               this.fields.put("submittable", wi.getField("deferred").getBoolean());
               this.fields.put("member", Boolean.TRUE);
               if ("si.DoomedMember".equals(wi.getModelType())) {
                  this.fields.put("dropped", Boolean.TRUE);
               }

               try {
                  String memberRev = null;
                  item = wi.getField("memberrev").getItem();
                  if (item != null) {
                     memberRev = item.getId();
                  }

                  this.fields.put("memberRev", memberRev);
                  String workingRev = null;
                  item = wi.getField("workingrev").getItem();
                  if (item != null) {
                     workingRev = item.getId();
                  }

                  this.fields.put("workingRev", workingRev);
                  item = wi.getField("wfdelta").getItem();
                  boolean wfDelta = false;
                  boolean wfDeltaSizeChanged = false;
                  boolean noWorkingFile = false;
                  if (item != null) {
                     Boolean wfDeltaObj = item.getField("isDelta").getBoolean();
                     if (wfDeltaObj != null) {
                        wfDelta = wfDeltaObj;
                     }

                     Boolean noWfObj = item.getField("noWorkingFile").getBoolean();
                     if (noWfObj != null) {
                        noWorkingFile = noWfObj;
                     }

                     Long currentSize = item.getField("workingFileSize").getLong();
                     if (currentSize != null) {
                        Long cachedSize = item.getField("cachedFileSize").getLong();
                        wfDeltaSizeChanged = !currentSize.equals(cachedSize);
                     }
                  }

                  item = wi.getField("revsyncdelta").getItem();
                  boolean revSyncDelta = false;
                  boolean isWorkingRevUnknown = false;
                  Boolean workingRevLockedByMe;
                  if (item != null) {
                     workingRevLockedByMe = item.getField("isDelta").getBoolean();
                     if (workingRevLockedByMe != null) {
                        revSyncDelta = workingRevLockedByMe;
                     }

                     Boolean isWorkingRevUnknownObj = item.getField("isWorkingRevUnknown").getBoolean();
                     if (isWorkingRevUnknownObj != null) {
                        isWorkingRevUnknown = isWorkingRevUnknownObj;
                     }

                     Boolean isWorkingRevPendingObj = item.getField("isWorkingRevPending").getBoolean();
                     if (isWorkingRevPendingObj != null) {
                        this.fields.put("pending", isWorkingRevPendingObj);
                     }
                  }

                  if (wfDelta && !noWorkingFile) {
                     this.fields.put("workingDelta", Boolean.TRUE);
                     if (wfDeltaSizeChanged) {
                        this.markModified();
                     }
                  }

                  if (noWorkingFile && isWorkingRevUnknown) {
                     this.fields.put("incoming", Boolean.TRUE);
                  }

                  if (revSyncDelta) {
                     this.fields.put("outOfDate", Boolean.TRUE);
                  }

                  workingRevLockedByMe = wi.getField("workingrevlockedbyme").getBoolean();
                  if (workingRevLockedByMe != null) {
                     this.fields.put("lockedByMe", workingRevLockedByMe);
                  }

                  List lockRecords = wi.getField("lockrecord").getList();
                  if (lockRecords != null) {
                     Iterator i = lockRecords.iterator();

                     while(i.hasNext()) {
                        Item lockRecord = (Item)i.next();
                        Item revision = lockRecord.getField("revision").getItem();
                        Item user = lockRecord.getField("locker").getItem();
                        String lockType = lockRecord.getField("locktype").getString();
                        if (user != null && revision != null && workingRev != null && workingRev.equals(revision.getId())) {
                           if (user.getId().equals(userId)) {
                              if ("exclusive".equals(lockType)) {
                                 this.fields.put("exclusiveLockedByMe", Boolean.TRUE);
                              }
                           } else {
                              this.fields.put("lockedByOther", Boolean.TRUE);
                              if ("exclusive".equals(lockType)) {
                                 this.fields.put("exclusiveLockedByOther", Boolean.TRUE);
                              }
                           }
                        }
                     }
                  }
               } catch (NoSuchElementException var26) {
               }

            }
         } else {
            this.fields.put("memberName", this.file);
            this.fields.put("sandbox", this.file.getAbsolutePath());
            exceptionName = wi.getField("type").getValueAsString();
            boolean shared = exceptionName.indexOf("shared") != -1;
            this.fields.put("shared", new Boolean(shared));
         }
      }
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (!(obj instanceof WorkingFile)) {
         return false;
      } else {
         WorkingFile wf = (WorkingFile)obj;
         if (this == wf) {
            return true;
         } else if ((this.file != null || wf.file == null) && this.file.equals(wf.file)) {
            if (this.fields.size() != wf.fields.size()) {
               return false;
            } else {
               Iterator it = this.fields.keySet().iterator();

               Object thisValue;
               Object wfValue;
               do {
                  if (!it.hasNext()) {
                     return true;
                  }

                  Object key = it.next();
                  thisValue = this.fields.get(key);
                  wfValue = wf.fields.get(key);
               } while((thisValue != null || wfValue == null) && (thisValue == null || thisValue.equals(wfValue)));

               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean isInvalid() {
      return !this.valid;
   }

   void invalidate() {
      this.valid = false;
   }

   public boolean isInSandboxDir() {
      return (Boolean)this.fields.get("inSandboxDir");
   }

   public boolean isControlled() {
      return (Boolean)this.fields.get("controlled");
   }

   public boolean isMember() {
      return (Boolean)this.fields.get("member");
   }

   public boolean isFormerMember() {
      return (Boolean)this.fields.get("formerMember");
   }

   public boolean isAdded() {
      return (Boolean)this.fields.get("added");
   }

   public boolean isDropped() {
      return (Boolean)this.fields.get("dropped");
   }

   public boolean isMoved() {
      return (Boolean)this.fields.get("moved");
   }

   public boolean isDeferred() {
      return this.getWorkingCpid() != null || this.isAdded() || this.isDropped() || this.isMoved();
   }

   public boolean hasWorkingDelta() {
      return (Boolean)this.fields.get("workingDelta");
   }

   void markModified() {
      this.fields.put("modified", Boolean.TRUE);
   }

   public boolean isModified() {
      return (Boolean)this.fields.get("modified");
   }

   public boolean isOutOfDate() {
      return (Boolean)this.fields.get("outOfDate");
   }

   public boolean isIncoming() {
      return (Boolean)this.fields.get("incoming");
   }

   public boolean isLockedByMe() {
      return (Boolean)this.fields.get("lockedByMe");
   }

   public boolean isLockedByOther() {
      return (Boolean)this.fields.get("lockedByOther");
   }

   public boolean isExclusiveLockByOther() {
      return (Boolean)this.fields.get("exclusiveLockedByOther");
   }

   public boolean isExclusiveLockByMe() {
      return (Boolean)this.fields.get("exclusiveLockedByMe");
   }

   public boolean isPending() {
      return (Boolean)this.fields.get("pending");
   }

   public boolean isShared() {
      return (Boolean)this.fields.get("shared");
   }

   public boolean isPendingAdd() {
      return (Boolean)this.fields.get("pendingAdd");
   }

   public File getMemberName() {
      return (File)this.fields.get("memberName");
   }

   public String getMemberRev() {
      return (String)this.fields.get("memberRev");
   }

   public File getFile() {
      return this.file;
   }

   public String getName() {
      return this.file.getAbsolutePath();
   }

   public String getSandbox() {
      return (String)this.fields.get("sandbox");
   }

   public String getWorkingCpid() {
      return (String)this.fields.get("workingCpid");
   }

   public String getWorkingRev() {
      return (String)this.fields.get("workingRev");
   }

   public File getWorkingRoot() {
      return this.workingRoot;
   }

   public String toString() {
      return this.getName();
   }

   public String statusString() {
      String string = "Working File: " + this.getName() + "\n" + " Member Name: " + this.getMemberName() + "\n" + " Sandbox: " + this.getSandbox() + "\n" + " Working Root: " + this.getWorkingRoot() + "\n" + " Working CPID: " + this.getWorkingCpid() + "\n" + " Member Rev: " + this.getMemberRev() + "\n" + " Working Rev: " + this.getWorkingRev() + "\n" + " Invalid: " + this.isInvalid() + "\n" + " In Sandbox Dir: " + this.isInSandboxDir() + "\n" + " isControlled: " + this.isControlled() + "\n" + " member: " + this.isMember() + "\n" + " former Member: " + this.isFormerMember() + "\n" + " added: " + this.isAdded() + "\n" + " dropped: " + this.isDropped() + "\n" + " moved: " + this.isMoved() + "\n" + " has WF Delta: " + this.hasWorkingDelta() + "\n" + " modified: " + this.isModified() + "\n" + " incoming: " + this.isIncoming() + "\n" + " outOfDate: " + this.isOutOfDate() + "\n" + " locked by me: " + this.isLockedByMe() + "\n" + " locked by other: " + this.isLockedByOther() + "\n" + " deferred: " + this.isDeferred() + "\n" + " exclusive by other: " + this.isExclusiveLockByOther() + "\n" + " exclusive by me: " + this.isExclusiveLockByMe() + "\n" + " pending: " + this.isPending() + "\n" + " pending Add: " + this.isPendingAdd() + "\n" + " ordinal: " + this.getOrdinal() + "\n";
      if (this.exception != null) {
         string = string + " exception: " + this.exception.getExceptionId() + "\n";
      }

      return string;
   }

   private void setAPIException(APIException exception) {
      this.exception = exception;
      this.valid = exception == null;
   }

   public APIException getAPIException() {
      return this.exception;
   }

   public long getOrdinal() {
      return this.ordinal;
   }

   public String getContext() {
      return this.getSandbox();
   }

   public String getContext(String key) {
      return null;
   }

   public Enumeration getContextKeys() {
      return new Enumeration() {
         public boolean hasMoreElements() {
            return false;
         }

         public Object nextElement() {
            return null;
         }
      };
   }

   public String getDisplayId() {
      return null;
   }

   public String getId() {
      String id = this.getName();
      if (WorkingFileFactory.isWin32()) {
         id = id.toLowerCase();
      }

      return id;
   }

   public String getModelType() {
      return "si.WorkingFile";
   }

   public boolean contains(String id) {
      return this.fields.containsKey(id);
   }

   public Field getField(String id) {
      Object fieldValue = null;
      if ("invalid".equals(id)) {
         fieldValue = this.isInvalid() ? Boolean.TRUE : Boolean.FALSE;
      } else if ("ordinal".equals(id)) {
         fieldValue = new Long(this.ordinal);
      } else {
         if (!this.contains(id)) {
            throw new NoSuchElementException();
         }

         fieldValue = this.fields.get(id);
      }

      SimpleResponseFactory factory = SimpleResponseFactory.getResponseFactory();
      ModifiableField field = factory.createField(id);
      field.setValue(fieldValue);
      return field;
   }

   public int getFieldListSize() {
      return this.fields.size() + 2;
   }

   public Iterator getFields() {
      List itemFields = new ArrayList(this.getFieldListSize());
      Iterator ids = this.fields.keySet().iterator();

      while(ids.hasNext()) {
         itemFields.add(this.getField((String)ids.next()));
      }

      itemFields.add(this.getField("invalid"));
      itemFields.add(this.getField("ordinal"));
      return itemFields.iterator();
   }
}
