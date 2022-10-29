package com.gameservice.tournament.model;

import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.model.BaseModel;
import com.gameservice.outcome.model.GameModel;
import com.gameservice.outcome.model.RoomModel;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tournaments")
public class TournamentModel extends BaseModel {

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private EnumConstants.TOURNAMENT_TYPE type;

    @Column(name = "buyin", nullable = false)
    private Integer buyin;

    @Column(name = "rake", nullable = false)
    private Integer rake;

    @Column(name = "prize", nullable = false)
    private Integer prize;

    @Column(name = "max_player", nullable = false)
    private Integer maxPlayer;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "order", nullable = false)
    private Integer order;

    @Column(name = "game", nullable = false)
    private EnumConstants.GAME_TYPE game;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @Column(name = "points_value", nullable = false)
    private Float pointsValue;

    @Column(name = "total_levels", nullable = false)
    private Integer totalLevels;

    @Column(name = "total_rounds", nullable = false)
    private Integer totalRounds;

    @Column(name = "total_time", nullable = false)
    private Integer totalTime;

    @Column(name = "break_time", nullable = false)
    private Integer breakTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RoomModel roomModel;

    @OneToMany(mappedBy = "tableModel")
    private Set<GameModel> gameModels = new HashSet<>();


}
