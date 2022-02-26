package br.com.sw2you.realmeet.util;

import br.com.sw2you.realmeet.exception.InvalidOrderByExceptionException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageUtils {

    private PageUtils() {}

    public static Pageable newPageable(
        Integer page,
        Integer limit,
        Integer maxLimit,
        String orderBy,
        List<String> validSortableFields
    ) {
        int definedPage = Objects.isNull(page) ? 0 : page;
        int definedLimit = Objects.isNull(limit) ? maxLimit : Math.min(limit, maxLimit);
        Sort sort = parseOrderByFields(orderBy, validSortableFields);

        return PageRequest.of(definedPage, definedLimit, sort);
    }

    private static Sort parseOrderByFields(String orderBy, List<String> validSortableFields) {
        // fieldA, -fieldB, fieldC

        if (Objects.isNull(validSortableFields) || validSortableFields.isEmpty()) {
            throw new IllegalArgumentException("No valid sortable fields were defined");
        }

        if (StringUtils.isBlank(orderBy)) {
            return Sort.unsorted();
        }

        var fields = orderBy.split(",");

        var listOrders = Arrays
            .stream(fields)
            .map(
                f -> {
                    if (!validSortableFields.contains(f.startsWith("-") ? f.substring(1) : f)) {
                        throw new InvalidOrderByExceptionException();
                    }
                    if (f.startsWith("-")) {
                        return Sort.Order.desc(f.substring(1));
                    } else {
                        return Sort.Order.asc(f);
                    }
                }
            )
            .collect(Collectors.toList());

        return Sort.by(listOrders);
    }
}
