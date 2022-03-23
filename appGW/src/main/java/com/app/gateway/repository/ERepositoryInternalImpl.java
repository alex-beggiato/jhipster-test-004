package com.app.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.app.gateway.domain.E;
import com.app.gateway.repository.rowmapper.CRowMapper;
import com.app.gateway.repository.rowmapper.ERowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the E entity.
 */
@SuppressWarnings("unused")
class ERepositoryInternalImpl extends SimpleR2dbcRepository<E, Long> implements ERepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CRowMapper cMapper;
    private final ERowMapper eMapper;

    private static final Table entityTable = Table.aliased("e", EntityManager.ENTITY_ALIAS);
    private static final Table entCCCuidTable = Table.aliased("c", "entCCCuid");

    public ERepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CRowMapper cMapper,
        ERowMapper eMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(E.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.cMapper = cMapper;
        this.eMapper = eMapper;
    }

    @Override
    public Flux<E> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<E> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<E> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ESqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CSqlHelper.getColumns(entCCCuidTable, "entCCCuid"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(entCCCuidTable)
            .on(Column.create("entcccuid_id", entityTable))
            .equals(Column.create("id", entCCCuidTable));

        String select = entityManager.createSelect(selectFrom, E.class, pageable, criteria);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<E> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<E> findById(Long id) {
        return createQuery(null, where(EntityManager.ENTITY_ALIAS + ".id").is(id)).one();
    }

    private E process(Row row, RowMetadata metadata) {
        E entity = eMapper.apply(row, "e");
        entity.setEntCCCuid(cMapper.apply(row, "entCCCuid"));
        return entity;
    }

    @Override
    public <S extends E> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
