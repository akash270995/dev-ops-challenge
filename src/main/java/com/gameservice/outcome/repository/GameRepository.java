package com.gameservice.outcome.repository;


import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.model.GameModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameModel, Long> {

    @Query("select count(g) from GameModel g inner join g.participantsModels participantsModels " +
            "where g.tableId = ?1 and g.status = ?2")
    long countPlayersByStatus(Long tableId, EnumConstants.GAME_STATUS status);

    @Query("select g from GameModel g inner join g.participantsModels participantsModels " +
            "where g.tableId = ?1 and g.status = ?2")
    List<GameModel> gameForAutoSpawn(Long tableId, EnumConstants.GAME_STATUS status, PageRequest pageRequest);

    @Query("select sum(g.totalRake) from GameModel g where g.createdAt >= ?1 and g.createdAt <= ?2")
    long totalRake(LocalDateTime from, LocalDateTime to);

}
