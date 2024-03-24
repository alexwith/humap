package com.github.alexwith.humap.repository.query;

import com.github.alexwith.humap.annotation.QuerySignature;
import com.mongodb.client.model.Filters;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bson.conversions.Bson;

public class QueryImpl implements Query {
    private final List<List<QueryField>> orGroup;

    private static final String PREFIX = "findBy";

    public QueryImpl(List<List<QueryField>> orGroup) {
        this.orGroup = orGroup;
    }

    public static Optional<Query> parse(Method method) {
        if (method.isDefault()) {
            return Optional.empty();
        }

        final String[] signature;
        if (method.isAnnotationPresent(QuerySignature.class)) {
            signature = method.getAnnotation(QuerySignature.class).value();
        } else {
            final String name = method.getName();
            if (!name.startsWith(PREFIX)) {
                return Optional.empty();
            }

            signature = name.replace(PREFIX, "").split("(?=\\p{Upper})");
        }

        final List<List<QueryField>> orGroup = new ArrayList<>();
        List<QueryField> currentAndGroup = new ArrayList<>();
        QueryOperator prefixOperator = null;
        for (final String value : signature) {
            final QueryOperator operator = QueryOperator.getOperator(value);
            if (operator == QueryOperator.AND) {
                continue;
            }

            if (operator == QueryOperator.OR) {
                orGroup.add(currentAndGroup);
                currentAndGroup = new ArrayList<>();
                continue;
            }

            if (operator != null) {
                prefixOperator = operator;
                continue;
            }

            final QueryOperator queryOperator = prefixOperator == null ? QueryOperator.EQUALS : prefixOperator;
            final QueryField queryField = new QueryField(value.toLowerCase(), queryOperator.getFilterMaker());
            currentAndGroup.add(queryField);
            prefixOperator = null;
        }

        orGroup.add(currentAndGroup);

        return Optional.of(new QueryImpl(orGroup));
    }

    @Override
    public Bson resolve(Object[] args) {
        int argIndex = 0;

        final Bson[] orFilters = new Bson[this.orGroup.size()];
        for (int i = 0; i < this.orGroup.size(); i++) {
            final List<QueryField> andGroup = this.orGroup.get(i);
            final Bson[] andFilters = new Bson[andGroup.size()];
            for (int j = 0; j < andGroup.size(); j++) {
                final QueryField field = andGroup.get(j);
                andFilters[j] = field.apply(args[argIndex++]);
            }

            orFilters[i] = andFilters.length == 1 ? andFilters[0] : Filters.and(andFilters);
        }

        return orFilters.length == 1 ? orFilters[0] : Filters.or(orFilters);
    }

    public static class QueryField {
        private final String field;
        private final QueryOperator.FilterMaker filterMaker;

        public QueryField(String field, QueryOperator.FilterMaker filterMaker) {
            this.field = field;
            this.filterMaker = filterMaker;
        }

        public Bson apply(Object... values) {
            final Object[] args = new Object[values.length + 1];
            args[0] = this.field;

            System.arraycopy(values, 0, args, 1, values.length);

            return this.filterMaker.make(args);
        }
    }
}
