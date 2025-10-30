package com.example.demo.reminder.exception;

import com.example.demo.common.exception.CommonException;

public class NoContactForSenderException extends CommonException {

  public NoContactForSenderException(String sender) {
    super("User has no %s for sending reminder".formatted(sender));
  }

}
