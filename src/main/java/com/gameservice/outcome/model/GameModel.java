package com.gameservice.outcome.model;

import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.turn.model.TurnModel;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "game")
public class GameModel extends BaseModel {

    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @Column(name = "status", nullable = false)
    private EnumConstants.GAME_STATUS status;

    @Column(name = "max_rejoin", nullable = false)
    private Integer maxRejoin;

    @Column(name = "current_deal", nullable = false)
    private Integer currentDeal;

    @Column(name = "total_deal", nullable = false)
    private Integer totalDeal;

    @Column(name = "total_wager", nullable = false)
    private Long totalWager;

    @Column(name = "total_rake", nullable = false)
    private Long totalRake;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TableModel tableModel;

    @OneToMany(mappedBy = "gameModel")
    private Set<ParticipantsModel> participantsModels = new HashSet<>();

    @OneToMany(mappedBy = "gameModel")
    private Set<DealsModel> dealsModels = new HashSet<>();

    @OneToMany(mappedBy = "gameModel")
    private Set<TurnModel> turnModels = new HashSet<>();

}
