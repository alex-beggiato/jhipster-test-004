package com.app.gateway.repository.rowmapper;

import com.app.gateway.domain.D;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link D}, with proper type conversions.
 */
@Service
public class DRowMapper implements BiFunction<Row, String, D> {

    private final ColumnConverter converter;

    public DRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link D} stored in the database.
     */
    @Override
    public D apply(Row row, String prefix) {
        D entity = new D();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUid(converter.fromRow(row, prefix + "_uid", UUID.class));
        entity.setEntDDDcod(converter.fromRow(row, prefix + "_ent_dd_dcod", String.class));
        entity.setEntDDDdsc(converter.fromRow(row, prefix + "_ent_dd_ddsc", String.class));
        return entity;
    }
}
