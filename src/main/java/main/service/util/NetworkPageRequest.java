package main.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class NetworkPageRequest extends PageRequest {

    protected NetworkPageRequest(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public static NetworkPageRequest of(int offset, int perPage) {
        return of(offset, perPage, Sort.unsorted());
    }

    public static NetworkPageRequest of(int offset, int perPage, Sort sort) {
        int page = offset / perPage;
        return new NetworkPageRequest(page, perPage, sort);
    }
}
