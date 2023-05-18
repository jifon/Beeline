package com.example.beeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Result {
    private String country;
    private List<String> cities;
    private String cities_count;
}
