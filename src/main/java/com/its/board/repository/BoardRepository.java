package com.its.board.repository;

import com.its.board.entity.BoardEntity;
import com.its.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

//    update board_table set board_hits=board_hits+1 where id=?
    @Modifying
//    update 또는 delete 쿼리 작성시에는 Modifying 어노테이션 필수로 선언해줘야함
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id=:paramId")
//    아래는 DB 컬럼에 직접 접근하는 방법(Entity로 접근하는것 권장)
//    @Query(value = "update board_table set board_hits=board_hits+1 where id=:paramId", nativeQuery = true)
    void updateHits(@Param("paramId") Long id);


//        select * from board_table where board_writer like '%q%' order by id desc; // 작성자 기준 검색어
    List<BoardEntity> findByBoardWriterContainingOrderByIdDesc(String q);

//        select * from board_table where board_title like '%q%' order by id desc; // 제목 기준 검색어
    List<BoardEntity> findByBoardTitleContainingOrderByIdDesc(String q);

//        select * from board_table where board_title like '%q%' or board_writer like '%q%' order by id desc; // 제목, 작성자 기준 검색어(통합검색)
    List<BoardEntity> findByBoardTitleContainingOrBoardWriterContainingOrderByIdDesc(String title, String writer);
//      쿼리상 q 라는 매개변수가 같은 값이긴 하지만 2개의 변수가 들어가기 때문에 매개변수를 2개 지정해줘야함

}
