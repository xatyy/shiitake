package ro.foodx.backend.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private int page;
    private int total;
    private boolean success;

}