package com.gameservice.turn.model;

import com.gameservice.outcome.model.BaseModel;
import com.gameservice.outcome.model.GameModel;
import com.gameservice.turn.constant.EnumConstants;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "turn")
public class TurnModel extends BaseModel {

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "turn_order", nullable = false)
    private Integer turnOrder;

    @Column(name = "turn_time")
    private LocalDateTime turnTime;

    @Column(name = "status")
    private EnumConstants.TURN_STATUS turnStatus = EnumConstants.TURN_STATUS.INACTIVE;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GameModel gameModel;

}
