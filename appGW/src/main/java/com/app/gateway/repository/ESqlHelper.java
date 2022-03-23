package com.app.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ESqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("uid", table, columnPrefix + "_uid"));
        columns.add(Column.aliased("ent_cc_cuid", table, columnPrefix + "_ent_cc_cuid"));
        columns.add(Column.aliased("ent_ee_ecod", table, columnPrefix + "_ent_ee_ecod"));
        columns.add(Column.aliased("ent_ee_edsc", table, columnPrefix + "_ent_ee_edsc"));

        columns.add(Column.aliased("entcccuid_id", table, columnPrefix + "_entcccuid_id"));
        return columns;
    }
}
