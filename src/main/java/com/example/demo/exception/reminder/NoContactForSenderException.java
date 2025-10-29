package com.example.demo.exception.reminder;

import com.example.demo.exception.base.BaseException;

public class NoContactForSenderException extends BaseException {

  public NoContactForSenderException(String sender) {
    super("User has no %s for sending reminder".formatted(sender));
  }

}
