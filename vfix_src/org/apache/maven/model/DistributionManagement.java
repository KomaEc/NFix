package org.apache.maven.model;

import java.io.Serializable;

public class DistributionManagement implements Serializable {
   private DeploymentRepository repository;
   private DeploymentRepository snapshotRepository;
   private Site site;
   private String downloadUrl;
   private Relocation relocation;
   private String status;

   public String getDownloadUrl() {
      return this.downloadUrl;
   }

   public Relocation getRelocation() {
      return this.relocation;
   }

   public DeploymentRepository getRepository() {
      return this.repository;
   }

   public Site getSite() {
      return this.site;
   }

   public DeploymentRepository getSnapshotRepository() {
      return this.snapshotRepository;
   }

   public String getStatus() {
      return this.status;
   }

   public void setDownloadUrl(String downloadUrl) {
      this.downloadUrl = downloadUrl;
   }

   public void setRelocation(Relocation relocation) {
      this.relocation = relocation;
   }

   public void setRepository(DeploymentRepository repository) {
      this.repository = repository;
   }

   public void setSite(Site site) {
      this.site = site;
   }

   public void setSnapshotRepository(DeploymentRepository snapshotRepository) {
      this.snapshotRepository = snapshotRepository;
   }

   public void setStatus(String status) {
      this.status = status;
   }
}
