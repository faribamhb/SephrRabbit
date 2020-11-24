package com.sepehr.sepehr.common.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchablePage {
    private int page;
    private int total;
    private String order;
    private String direction;
    private Map<String, String> filter;
    private PageRequest pageRequest;
    private List<Searchable> searchables;
}
