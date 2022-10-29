package com.gameservice.outcome.repository;

import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.model.HandsModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HandsRepository extends JpaRepository<HandsModel, Long> {

    @Query("select h from HandsModel h where h.dealId = ?1 and h.cardsOwner = ?2 and h.id = (select max(h2.id) from HandsModel h2 where h2.participantId = h.participantId) order by h.id DESC")
    List<HandsModel> getLastHands(Long dealId, EnumConstants.CARDS_OWNER cardsOwner);

    @Query("select h from HandsModel h where h.dealId = ?1 and h.participantsModel.gameId = ?2 and h.cardsOwner = ?3 and h.id = (select max(h2.id) from HandsModel h2 where h2.participantId = h.participantId) order by h.id DESC")
    List<HandsModel> getLastHand(Long dealId, Long gameId, EnumConstants.CARDS_OWNER cardsOwner, Pageable pageable);

    @Query("select h from HandsModel h where h.dealId = ?1 and h.participantsModel.gameId = ?2 and h.cardsOwner = ?3 and h.participantsModel.userId = ?4 and h.id = (select max(h2.id) from HandsModel h2 where h2.participantId = h.participantId) order by h.id DESC")
    List<HandsModel> getLastHandByPlayerId(Long dealId, Long gameId, EnumConstants.CARDS_OWNER cardsOwner, Long userId, Pageable pageable);

    @Query("select h from HandsModel h where h.dealId = ?1 and h.cardsOwner = ?2 and h.deckType = ?3")
    List<HandsModel> getTableDeck(Long dealId, EnumConstants.CARDS_OWNER cardsOwner, EnumConstants.DECK_TYPE deckType, Pageable pageable);

}
