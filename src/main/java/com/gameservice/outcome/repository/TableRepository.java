package com.gameservice.outcome.repository;

import com.gameservice.outcome.model.TableModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends CrudRepository<TableModel, Long>, JpaSpecificationExecutor<TableModel> {

    @Query("select t.minPlayer from TableModel t where t.id = ?1")
    Long findTotalTablePlayers(Long tableId);

}
