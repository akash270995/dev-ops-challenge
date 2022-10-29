package com.gameservice.outcome.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rejoin")
public class RejoinModel extends BaseModel {

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "deal_id", nullable = false)
    private Long dealId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ParticipantsModel participantsModel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deal_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DealsModel dealsModel;

}
