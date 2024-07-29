package com.bashkir777.mailservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmailTemplateName {
    CONFIRMATION_EMAIL("confirmation", "Your Confirmation Code");
    private final String name;
    private final String subject;
}
