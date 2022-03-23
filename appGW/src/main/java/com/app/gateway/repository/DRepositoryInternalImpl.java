package com.app.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.app.gateway.domain.D;
import com.app.gateway.repository.rowmapper.DRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the D entity.
 */
@SuppressWarnings("unused")
class DRepositoryInternalImpl extends SimpleR2dbcRepository<D, Long> implements DRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DRowMapper dMapper;

    private static final Table entityTable = Table.aliased("d", EntityManager.ENTITY_ALIAS);

    public DRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DRowMapper dMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(D.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.dMapper = dMapper;
    }

    @Override
    public Flux<D> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<D> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<D> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = DSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, D.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<D> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<D> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private D process(Row row, RowMetadata metadata) {
        D entity = dMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends D> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
