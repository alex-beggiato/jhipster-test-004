package com.app.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("uid", table, columnPrefix + "_uid"));
        columns.add(Column.aliased("ent_dd_dcod", table, columnPrefix + "_ent_dd_dcod"));
        columns.add(Column.aliased("ent_dd_ddsc", table, columnPrefix + "_ent_dd_ddsc"));

        return columns;
    }
}
