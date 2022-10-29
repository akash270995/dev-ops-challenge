package com.gameservice.outcome.repository;

import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.model.ParticipantsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ParticipantsRepository extends JpaRepository<ParticipantsModel, Long> {

    @Query("select p from ParticipantsModel p where p.gameId = ?1 and p.playerStatus != ?2")
    List<ParticipantsModel> findByGameId(Long gameId, EnumConstants.PLAYER_STATUS playerStatus);

    @Query("select p from ParticipantsModel p where p.gameId = ?1 and p.userId = ?2 and p.playerStatus != ?3")
    ParticipantsModel findByGameIdAndUserId(Long gameId, Long userId, EnumConstants.PLAYER_STATUS playerStatus);

    @Transactional
    @Modifying
    @Query("update ParticipantsModel p set p.eliminated = true where p.gameId = ?1 and p.userId = ?2")
    void eliminatePlayer(Long gameId, Long userId);

    @Transactional
    @Modifying
    @Query("update ParticipantsModel p set p.wager = ?3 where p.gameId = ?1 and p.userId = ?2")
    void updateParticipantWager(Long gameId, Long userId, Long wager);

    @Query("select count(p) from ParticipantsModel p where p.gameId = ?1")
    Long findTotalPlayersOnTable(Long gameId);



}
