package com.gameservice.turn.repository;

import com.gameservice.turn.constant.EnumConstants;
import com.gameservice.turn.model.TurnModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TurnRepository extends JpaRepository<TurnModel, Long> {

    @Query("select t from TurnModel t where t.gameId = ?1 order by t.turnOrder ASC")
    List<TurnModel> findByGameId(Long gameId);

    @Query("select t from TurnModel t where t.gameId = ?1 and t.userId = ?2 order by t.turnOrder ASC")
    TurnModel findByGameIdAndUserId(Long gameId, Long userId);

}
