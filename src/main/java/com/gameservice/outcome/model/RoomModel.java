package com.gameservice.outcome.model;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "room")
public class RoomModel extends BaseModel {

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "room_type")
    private EnumConstants.ROOM_TYPE roomType;

    @Column(name = "max_tables")
    private Integer maxTables;

    @OneToMany(mappedBy = "roomModel")
    private Set<TableModel> tableModels = new HashSet<>();

}
