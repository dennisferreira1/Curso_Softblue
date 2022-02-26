package br.com.sw2you.realmeet.unit;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.exception.InvalidOrderByExceptionException;
import br.com.sw2you.realmeet.util.PageUtils;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class PageUtilsUnitTest extends BaseUnitTest {

    @Test
    void testNewPageableWhenPageIsNullAndLimitIsNullAndOrderByIsNull() {
        var pageable = PageUtils.newPageable(null, null, 10, null, Allocation.SORTABLE_FIELDS);
        Assertions.assertEquals(0, pageable.getPageNumber());
        Assertions.assertEquals(10, pageable.getPageSize());
        Assertions.assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void testNewPageableWhenPageIsNegative() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> PageUtils.newPageable(-1, null, 10, null, Allocation.SORTABLE_FIELDS)
        );
    }

    @Test
    void testNewPageableWhenLimitIsInvalid() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> PageUtils.newPageable(-1, 0, 10, null, Allocation.SORTABLE_FIELDS)
        );
    }

    @Test
    void testNewPageableWhenLimitExceedsMaximum() {
        var pageable = PageUtils.newPageable(null, 11, 10, null, Allocation.SORTABLE_FIELDS);
        Assertions.assertEquals(10, pageable.getPageSize());
    }

    @Test
    void testNewPageableWhenSortableFieldsIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> PageUtils.newPageable(-1, 0, 10, null, null));
    }

    @Test
    void testNewPageableWhenSortableFieldsIsEmpty() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> PageUtils.newPageable(-1, 0, 10, null, Collections.emptyList())
        );
    }

    @Test
    void testNewPageableWhenOrderByAscIsValid() {
        var pageable = PageUtils.newPageable(
            null,
            null,
            10,
            Allocation.SORTABLE_FIELDS.get(0),
            Allocation.SORTABLE_FIELDS
        );
        Assertions.assertEquals(Sort.by(Sort.Order.asc(Allocation.SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

    @Test
    void testNewPageableWhenOrderByDescIsValid() {
        var pageable = PageUtils.newPageable(
            null,
            null,
            10,
            "-" + Allocation.SORTABLE_FIELDS.get(0),
            Allocation.SORTABLE_FIELDS
        );
        Assertions.assertEquals(Sort.by(Sort.Order.desc(Allocation.SORTABLE_FIELDS.get(0))), pageable.getSort());
    }

    @Test
    void testNewPageableWhenOrderByFieldIsInvalid() {
        Assertions.assertThrows(
            InvalidOrderByExceptionException.class,
            () -> PageUtils.newPageable(null, null, 10, "invalid", Allocation.SORTABLE_FIELDS)
        );
    }
}
