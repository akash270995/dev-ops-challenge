package com.gameservice.outcome.model;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tables")
public class TableModel extends BaseModel {

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "game_type", nullable = false)
    private EnumConstants.RUMMY_TYPE gameType;

    @Column(name = "game_sub_type", nullable = false)
    private EnumConstants.GAME_SUB_TYPE gameSubType;

    @Column(name = "min_player", nullable = false)
    private Integer minPlayer;

    @Column(name = "max_player", nullable = false)
    private Integer maxPlayer;

    @Column(name = "min_buyin", nullable = false)
    private Integer minBuyin;

    @Column(name = "max_buyin", nullable = false)
    private Integer maxBuyin;

    @Column(name = "deck", nullable = false)
    private Integer deck;

    @Column(name = "rake", nullable = false)
    private Integer rake;

    @Column(name = "table_order", nullable = false)
    private Integer tableOrder;

    @Column(name = "type", nullable = false)
    private EnumConstants.TABLE_TYPE type;

    @Column(name = "total_deals", nullable = false)
    private Integer totalDeals;

    @Column(name = "max_rejoin", nullable = false)
    private Integer maxRejoin;

    @Column(name = "game", nullable = false)
    private EnumConstants.GAME_TYPE game;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @Column(name = "points_value", nullable = false)
    private Float pointsValue;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RoomModel roomModel;

    @OneToMany(mappedBy = "tableModel")
    private Set<GameModel> gameModels = new HashSet<>();


}
