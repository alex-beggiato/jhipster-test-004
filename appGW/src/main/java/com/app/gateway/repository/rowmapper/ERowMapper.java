package com.app.gateway.repository.rowmapper;

import com.app.gateway.domain.E;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link E}, with proper type conversions.
 */
@Service
public class ERowMapper implements BiFunction<Row, String, E> {

    private final ColumnConverter converter;

    public ERowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link E} stored in the database.
     */
    @Override
    public E apply(Row row, String prefix) {
        E entity = new E();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUid(converter.fromRow(row, prefix + "_uid", UUID.class));
        entity.setEntCCCuid(converter.fromRow(row, prefix + "_ent_cc_cuid", Long.class));
        entity.setEntEEEcod(converter.fromRow(row, prefix + "_ent_ee_ecod", String.class));
        entity.setEntEEEdsc(converter.fromRow(row, prefix + "_ent_ee_edsc", String.class));
        entity.setEntCCCuidId(converter.fromRow(row, prefix + "_entcccuid_id", Long.class));
        return entity;
    }
}
