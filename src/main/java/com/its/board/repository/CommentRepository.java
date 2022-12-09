package com.its.board.repository;

import com.its.board.dto.CommentDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
//    select * from comment_table where board_id=#{?} oredr by id desc;
//    select * from comment_table where board_id=boardEntity oredr by id desc;
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);

}
