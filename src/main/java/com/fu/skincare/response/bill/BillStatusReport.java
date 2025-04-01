package com.fu.skincare.response.bill;

public interface BillStatusReport {

  public int getTotal();

  public int getPending();

  public int getApproved();

  public int getCanceled();

  public int getRejected();

  public int getSuccess();

  public int getDone();
}
