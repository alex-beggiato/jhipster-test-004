package com.app.gateway.repository.rowmapper;

import com.app.gateway.domain.B;
import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link B}, with proper type conversions.
 */
@Service
public class BRowMapper implements BiFunction<Row, String, B> {

    private final ColumnConverter converter;

    public BRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link B} stored in the database.
     */
    @Override
    public B apply(Row row, String prefix) {
        B entity = new B();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUid(converter.fromRow(row, prefix + "_uid", UUID.class));
        entity.setEntAAAuid(converter.fromRow(row, prefix + "_ent_aa_auid", Long.class));
        entity.setEntBBBcod(converter.fromRow(row, prefix + "_ent_bb_bcod", String.class));
        entity.setEntBBBdsc(converter.fromRow(row, prefix + "_ent_bb_bdsc", String.class));
        entity.setEntAAAuidId(converter.fromRow(row, prefix + "_entaaauid_id", Long.class));
        return entity;
    }
}
