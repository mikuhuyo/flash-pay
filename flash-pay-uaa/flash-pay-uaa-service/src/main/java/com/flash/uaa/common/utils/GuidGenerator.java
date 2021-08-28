package com.flash.uaa.common.utils;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;

import java.util.UUID;

public abstract class GuidGenerator {
    private static RandomValueStringGenerator defaultClientSecretGenerator = new RandomValueStringGenerator(32);

    private GuidGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateClientSecret() {
        return defaultClientSecretGenerator.generate();
    }

}
