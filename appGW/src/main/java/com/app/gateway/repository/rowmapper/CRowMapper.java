package com.app.gateway.repository.rowmapper;

import com.app.gateway.domain.C;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link C}, with proper type conversions.
 */
@Service
public class CRowMapper implements BiFunction<Row, String, C> {

    private final ColumnConverter converter;

    public CRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link C} stored in the database.
     */
    @Override
    public C apply(Row row, String prefix) {
        C entity = new C();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUid(converter.fromRow(row, prefix + "_uid", UUID.class));
        entity.setEntCCCcod(converter.fromRow(row, prefix + "_ent_cc_ccod", String.class));
        entity.setEntCCCdsc(converter.fromRow(row, prefix + "_ent_cc_cdsc", String.class));
        return entity;
    }
}
