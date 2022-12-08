package com.its.board.repository;

import com.its.board.dto.CommentDTO;
import com.its.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllById(Long boardId);
}
