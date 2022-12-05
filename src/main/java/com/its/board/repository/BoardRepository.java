package com.its.board.repository;

import com.its.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @Modifying
    @Query("update BoardEntity b set b.boardHits = b.boardHits+1 where b.id=:id")
    void updateHits(Long id);

}
