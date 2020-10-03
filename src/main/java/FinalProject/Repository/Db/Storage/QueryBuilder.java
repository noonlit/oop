package FinalProject.Repository.Db.Storage;

import FinalProject.Exception.CannotBuildQueryException;

import java.util.*;

public class QueryBuilder {
    private String select;
    private String from;
    private Map<String, Object> filters = new HashMap<>();
    private List<String> literalFilters = new ArrayList<>();
    private String limit;

    public QueryBuilder select() {
        select = "SELECT * ";
        return this;
    }

    public QueryBuilder from(String table) {
        from = " FROM " + table;
        return this;
    }

    public QueryBuilder where(String condition, Object bind) {
        filters.put(condition, bind);
        return this;
    }

    public QueryBuilder where(String condition) {
        literalFilters.add(condition);
        return this;
    }

    public QueryBuilder limit(int limit) {
        this.limit = " LIMIT " + Math.max(limit, 0);
        return this;
    }

    public Collection<Object> getBinds() {
        return filters.values();
    }

    /**
     * Concatenates the string fields of this class into valid SQL.
     */
    public String getQuery() {
        if ((select == null || select.isEmpty() || (from == null || from.isEmpty()))) {
            throw new CannotBuildQueryException("Invalid query!");
        }

        StringBuilder query = new StringBuilder(select + from);

        if (!filters.isEmpty() || !literalFilters.isEmpty()) {
            query.append(" WHERE ");
            for (Object filter : filters.keySet()) {
                query.append(filter);
                query.append(" AND ");
            }

            for (String literalFilter : literalFilters) {
                query.append(literalFilter);
                query.append(" AND ");
            }

            query.setLength(query.length() - 4);
        }

        if (limit == null) {
            return query.toString();
        }

        return query.append(limit).toString();
    }
}
