package com.its.board.repository;

import com.its.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

//    update board_table set board_hits=board_hits+1 where id=?
    @Modifying
//    update 또는 delete 쿼리 작성시에는 Modifying 어노테이션 필수로 선언해줘야함
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id=:paramId")
//    아래는 DB 컬럼에 직접 접근하는 방법(Entity로 접근하는것 권장)
//    @Query(value = "update board_table set board_hits=board_hits+1 where id=:paramId", nativeQuery = true)
    void updateHits(@Param("paramId") Long id);

}
