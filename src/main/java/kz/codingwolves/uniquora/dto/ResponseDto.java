package kz.codingwolves.uniquora.dto;

import java.util.List;

/**
 * Created by sagynysh on 1/7/17.
 */
public class ResponseDto<T> {

    public Integer page;

    public Integer totalPages;

    public Integer count;

    public List<T> data;

    public ResponseDto(Integer page, Integer totalPages, List<T> data) {
        this.page = page;
        this.totalPages = totalPages;
        this.data = data;
        this.count = data.size();
    }
}
