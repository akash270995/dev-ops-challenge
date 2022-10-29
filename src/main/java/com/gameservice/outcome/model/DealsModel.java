package com.gameservice.outcome.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "deals")
public class DealsModel extends BaseModel {

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "wild_joker", nullable = false)
    private Integer wildJoker;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GameModel gameModel;

    @OneToMany(mappedBy = "dealsModel")
    private Set<HandsModel> handsModels = new HashSet<>();

    @OneToMany(mappedBy = "dealsModel")
    private Set<OutcomeModel> outcomeModels = new HashSet<>();

    @OneToMany(mappedBy = "dealsModel")
    private Set<RejoinModel> rejoinModels = new HashSet<>();

}
