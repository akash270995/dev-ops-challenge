package com.gameservice.outcome.model;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "participants")
public class ParticipantsModel extends BaseModel {

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_connected", nullable = false)
    private Boolean connected;

    @Column(name = "player_status", nullable = false)
    private EnumConstants.PLAYER_STATUS playerStatus;

    @Column(name = "is_eliminated", nullable = false)
    private Boolean eliminated = false;

    @Column(name = "wager", nullable = false)
    private Long wager;

    @Column(name = "sub_wallet_balance", nullable = false)
    private Long subWalletBalance;

    @Column(name = "topup_wallet_balance", nullable = false)
    private Long topupWalletBalance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GameModel gameModel;

    @OneToMany(mappedBy = "participantsModel")
    private Set<HandsModel> handsModels = new HashSet<>();

    @OneToMany(mappedBy = "participantsModel")
    private Set<RejoinModel> rejoinModels = new HashSet<>();

}
