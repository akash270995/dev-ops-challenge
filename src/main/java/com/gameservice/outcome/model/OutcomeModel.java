package com.gameservice.outcome.model;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "outcome")
public class OutcomeModel extends BaseModel {

    @Column(name = "deal_id", nullable = false)
    private Long dealId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_winner", nullable = false)
    private Boolean winner;

    @Column(name = "is_rejoin", nullable = false)
    private Boolean rejoin = false;

    @Column(name = "player_rank", nullable = false)
    private Integer playerRank;

    @Column(name = "type", nullable = false)
    private EnumConstants.OUTCOME_TYPE outcomeType;

    @Column(name = "winning_amount", nullable = false)
    private Long winningAmount;

    @Column(name = "game_score", nullable = false)
    private Integer gameScore;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deal_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DealsModel dealsModel;


}
