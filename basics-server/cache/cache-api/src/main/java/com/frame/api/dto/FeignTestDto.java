package com.frame.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class FeignTestDto implements Serializable {
    private String name;
    private int age;
}
