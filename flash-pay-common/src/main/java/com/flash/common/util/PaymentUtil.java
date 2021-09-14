package com.flash.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public class PaymentUtil {

    private static final Pattern pattern = Pattern.compile("SJPAY(,\\S+){4}");
    private static final String SHANJUPAY_PREFIX = "SJ";

    public static boolean checkPayOrderAttach(String attach) {
        if (StringUtils.isBlank(attach)) {
            return false;
        }
        return pattern.matcher(attach).matches();
    }

    public static String genUniquePayOrderNo() {
        return SHANJUPAY_PREFIX + WorkId();
    }

    public static String WorkId() {
        long id = IdWorkerUtils.getInstance().nextId();
        return String.valueOf(id);
    }

}
