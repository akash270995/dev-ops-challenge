package com.gameservice.outcome.repository;

import com.gameservice.outcome.model.DealsModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealsRepository extends JpaRepository<DealsModel, Long> {

    @Query("select d from DealsModel d where d.gameId = ?1")
    List<DealsModel> getPreviousDeal(Long gameId, Pageable pageable);

}
