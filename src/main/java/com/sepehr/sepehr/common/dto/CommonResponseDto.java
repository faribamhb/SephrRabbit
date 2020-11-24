package com.sepehr.sepehr.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto implements Serializable {

    @Builder.Default
    private RESULT result = RESULT.SUCCESS;
    private String data;
    private int errorCode;

    public enum RESULT {SUCCESS, FAILURE}
}
