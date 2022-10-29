package com.gameservice.outcome.model;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "hands")
public class HandsModel extends BaseModel {

    @Column(name = "deal_id", nullable = false)
    private Long dealId;

    @Column(name = "cards_owner", nullable = false)
    private EnumConstants.CARDS_OWNER cardsOwner;

    @Column(name = "participant_id")
    private Long participantId;

    @Column(name = "hands")
    @ElementCollection(targetClass = Integer.class)
    private List<Integer> hands;

    @Column(name = "sets")
    @ElementCollection(targetClass = Integer.class)
    private List<Integer> sets;

    @Column(name = "pure_sequences")
    @ElementCollection(targetClass = Integer.class)
    private List<Integer> pureSequences;

    @Column(name = "impure_sequences")
    @ElementCollection(targetClass = Integer.class)
    private List<Integer> impureSequences;

    @Column(name = "invalid_cards")
    @ElementCollection(targetClass = Integer.class)
    private List<Integer> invalidCards;

    @Column(name = "card_dropped")
    private Integer cardDropped;

    @Column(name = "card_picked")
    private Integer cardPicked;

    @Column(name = "picked_from")
    private EnumConstants.PICKED_FROM pickedFrom;

    @Column(name = "final_hand")
    private Boolean finalHand;

    @Column(name = "pot_amount")
    private Integer potAmount;

    @Column(name = "rake")
    private Integer rake;

    @Column(name = "wager")
    private Integer wager;

    @Column(name = "current_turn_number")
    private Integer currentTurnNumber;

    @Column(name = "current_disconnected_turn")
    private Integer currentDisconnectedTurn;

    @Column(name = "is_disconnected")
    private Boolean disconnected;

    @Column(name = "is_auto_wager")
    private Boolean autoWager;

    @Column(name = "turn_start_time")
    private LocalDateTime turnStartTime;

    @Column(name = "turn_end_time")
    private LocalDateTime turnEndTime;

    @Column(name = "drop_type")
    private EnumConstants.DROP_TYPE dropType = EnumConstants.DROP_TYPE.NONE;

    @Column(name = "dropped_by")
    private EnumConstants.DROPPED_BY droppedBy;

    @Column(name = "deck_type")
    private EnumConstants.DECK_TYPE deckType = EnumConstants.DECK_TYPE.NONE;

    @Column(name = "declaration_type", nullable = false)
    private EnumConstants.DECLARATION_TYPE declarationType = EnumConstants.DECLARATION_TYPE.NONE;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deal_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DealsModel dealsModel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ParticipantsModel participantsModel;

}
