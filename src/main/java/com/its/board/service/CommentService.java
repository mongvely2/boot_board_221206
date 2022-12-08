package com.its.board.service;

import com.its.board.dto.BoardDTO;
import com.its.board.dto.CommentDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.CommentEntity;
import com.its.board.repository.BoardRepository;
import com.its.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public void commentSave(CommentDTO commentDTO) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        BoardEntity entity = optionalBoardEntity.get();

        CommentEntity commentEntity = CommentEntity.toSaveCommentEntity(commentDTO, entity);
        commentRepository.save(commentEntity);
    }

    public List<CommentDTO> findAll(Long boardId) {
        System.out.println("boardId = " + boardId);
        List<CommentEntity> commentEntityList = commentRepository.findAllById(boardId);
        System.out.println("commentEntityList = " + commentEntityList);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
        }
        return commentDTOList;
    }
}
