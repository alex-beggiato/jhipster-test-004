package com.app.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.app.gateway.domain.B;
import com.app.gateway.repository.rowmapper.ARowMapper;
import com.app.gateway.repository.rowmapper.BRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the B entity.
 */
@SuppressWarnings("unused")
class BRepositoryInternalImpl extends SimpleR2dbcRepository<B, Long> implements BRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ARowMapper aMapper;
    private final BRowMapper bMapper;

    private static final Table entityTable = Table.aliased("b", EntityManager.ENTITY_ALIAS);
    private static final Table entAAAuidTable = Table.aliased("a", "entAAAuid");

    public BRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ARowMapper aMapper,
        BRowMapper bMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(B.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.aMapper = aMapper;
        this.bMapper = bMapper;
    }

    @Override
    public Flux<B> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<B> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<B> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = BSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ASqlHelper.getColumns(entAAAuidTable, "entAAAuid"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(entAAAuidTable)
            .on(Column.create("entaaauid_id", entityTable))
            .equals(Column.create("id", entAAAuidTable));

        String select = entityManager.createSelect(selectFrom, B.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<B> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<B> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private B process(Row row, RowMetadata metadata) {
        B entity = bMapper.apply(row, "e");
        entity.setEntAAAuid(aMapper.apply(row, "entAAAuid"));
        return entity;
    }

    @Override
    public <S extends B> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
