package com.app.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("uid", table, columnPrefix + "_uid"));
        columns.add(Column.aliased("ent_aa_auid", table, columnPrefix + "_ent_aa_auid"));
        columns.add(Column.aliased("ent_bb_bcod", table, columnPrefix + "_ent_bb_bcod"));
        columns.add(Column.aliased("ent_bb_bdsc", table, columnPrefix + "_ent_bb_bdsc"));

        columns.add(Column.aliased("entaaauid_id", table, columnPrefix + "_entaaauid_id"));
        return columns;
    }
}
