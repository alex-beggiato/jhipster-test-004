package com.app.gateway.repository;

import com.app.gateway.domain.E;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the E entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ERepository extends ReactiveCrudRepository<E, Long>, ERepositoryInternal {
    @Query("SELECT * FROM e entity WHERE entity.entcccuid_id = :id")
    Flux<E> findByEntCCCuid(Long id);

    @Query("SELECT * FROM e entity WHERE entity.entcccuid_id IS NULL")
    Flux<E> findAllWhereEntCCCuidIsNull();

    @Override
    <S extends E> Mono<S> save(S entity);

    @Override
    Flux<E> findAll();

    @Override
    Mono<E> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ERepositoryInternal {
    <S extends E> Mono<S> save(S entity);

    Flux<E> findAllBy(Pageable pageable);

    Flux<E> findAll();

    Mono<E> findById(Long id);

    Flux<E> findAllBy(Pageable pageable, Criteria criteria);
}
