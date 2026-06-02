package com.devgui.urlshortener.api.dto.response;

import java.util.List;

public record PageResponse<T> (
        List<T> content,
        Integer page,
        Integer size,
        Integer totalPages,
        Long totalElements
){

}
