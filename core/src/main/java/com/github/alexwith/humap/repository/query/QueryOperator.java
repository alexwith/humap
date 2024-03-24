package com.github.alexwith.humap.repository.query;

import com.mongodb.client.model.Filters;
import java.util.HashMap;
import java.util.Map;
import org.bson.conversions.Bson;

public enum QueryOperator {

    AND(
        (args) -> Filters.and((Bson[]) args),
        "&", "and"
    ),
    OR(
        (args) -> Filters.or((Bson[]) args),
        "|", "or"
    ),
    EQUALS(
        (args) -> Filters.eq((String) args[0], args[1])
    ),
    LESS_THAN(
        (args) -> Filters.lt((String) args[0], args[1]),
        "<", "lt"
    ),
    LESS_THAN_EQ(
        (args) -> Filters.lte((String) args[0], args[1]),
        "<=", "lte"
    ),
    GREATER_THAN(
        (args) -> Filters.gt((String) args[0], args[1]),
        ">", "gt"
    ),
    GREATER_THAN_EQ(
        (args) -> Filters.gte((String) args[0], args[1]),
        ">=", "gte"
    );

    private final String[] identifiers;
    private final FilterMaker filterMaker;

    private static final Map<String, QueryOperator> MAPPED_OPERATORS = new HashMap<>();

    static {
        for (final QueryOperator operator : QueryOperator.values()) {
            for (final String identifier : operator.getIdentifiers()) {
                MAPPED_OPERATORS.put(identifier.toLowerCase(), operator);
            }
        }
    }

    QueryOperator(FilterMaker filterMaker, String... identifiers) {
        this.identifiers = identifiers;
        this.filterMaker = filterMaker;
    }

    public static QueryOperator getOperator(String identifier) {
        return MAPPED_OPERATORS.get(identifier.toLowerCase());
    }

    public String[] getIdentifiers() {
        return this.identifiers;
    }

    public FilterMaker getFilterMaker() {
        return this.filterMaker;
    }

    public interface FilterMaker {

        Bson make(Object... args);
    }
}