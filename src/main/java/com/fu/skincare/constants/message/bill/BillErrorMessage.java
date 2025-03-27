package com.fu.skincare.constants.message.bill;

public final class BillErrorMessage {
  public static final String NOT_FOUND = "Bill not found";
  public static final String EMPTY = "List Bills is empty";
  public static final String CANCELED = "Bill just canceled when status is PENDING or APPROVED";
  public static final String REJECTED = "Bill just rejected when status is PENDING";
  public static final String APPROVED = "Bill just approved when status is PENDING";
  public static final String DONE = "Bill just approved when status is SUCCESS";
}
