package com.app.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ASqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("uid", table, columnPrefix + "_uid"));
        columns.add(Column.aliased("ent_aa_acod", table, columnPrefix + "_ent_aa_acod"));
        columns.add(Column.aliased("ent_aa_adsc", table, columnPrefix + "_ent_aa_adsc"));

        return columns;
    }
}
