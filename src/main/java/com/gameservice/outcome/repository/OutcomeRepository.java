package com.gameservice.outcome.repository;

import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.model.OutcomeModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutcomeRepository extends JpaRepository<OutcomeModel, Long> {

    @Query("select o from OutcomeModel o where o.dealId = ?1")
    List<OutcomeModel> findByDealId(Long dealId);

    @Query("select o from OutcomeModel o where o.dealId = ?1 and o.outcomeType= ?2")
    List<OutcomeModel> findByDealId(Long dealId, EnumConstants.OUTCOME_TYPE outcomeType, Sort sort);

}
