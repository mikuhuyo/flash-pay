package com.flash.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestParams {
    private long startRow;
    private long limit;

    public static PageRequestParams of(Integer pageNo, Integer pageSize) {
        Long startRow = Long.valueOf((pageNo - 1) * pageSize);
        Long limit = Long.valueOf((pageSize));
        return new PageRequestParams(startRow, limit);
    }
}
