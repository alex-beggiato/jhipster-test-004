package com.app.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("uid", table, columnPrefix + "_uid"));
        columns.add(Column.aliased("ent_cc_ccod", table, columnPrefix + "_ent_cc_ccod"));
        columns.add(Column.aliased("ent_cc_cdsc", table, columnPrefix + "_ent_cc_cdsc"));

        return columns;
    }
}
