package com.its.board.service;

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

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {
//        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
//        get 으로 받아오면 Optional 없이 바로 boardEntity로 받아올 수 있음(아래 코드 참고)
        BoardEntity boardEntity = boardRepository.findById(commentDTO.getBoardId()).get();
        CommentEntity commentEntity = CommentEntity.toSaveCommentEntity(commentDTO, boardEntity);
        Long id = commentRepository.save(commentEntity).getId();
        return id;
    }

    //    public void commentSave(CommentDTO commentDTO) {
//        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
//        BoardEntity entity = optionalBoardEntity.get();
//
//        CommentEntity commentEntity = CommentEntity.toSaveCommentEntity(commentDTO, entity);
//        commentRepository.save(commentEntity);
//    }

    @Transactional
    public List<CommentDTO> findAll(Long boardId) {
//     댓글 리스트 호출을 위한 쿼리문: select*from comment_table where board_id = #{id}

//        댓글 목록 가져올 수 있는 2가지 방법
//        1. comment_table에서 직접 해당 게시글의 댓글 목록을 가져오기
//        ※ comment save시 참조키(board_id)를 객체 통으로 db에 저장했기 때문에 조회할때도 객체 통으로 다 필요함
        BoardEntity boardEntity = boardRepository.findById(boardId).get();      // 이 코드는 1,2 둘 다 필요함
        List<CommentEntity> commentEntityList =
                commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity); // repository 생성
//        2. BoardEntity를 조회해서 댓글 목록 가져오기
//        List<CommentEntity> commentEntityList1 = boardEntity.getCommentEntityList();    // Transactional 선언!!!
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
        }
        return commentDTOList;

    }

//    public List<CommentDTO> findAll(Long boardId) {
//        System.out.println("boardId = " + boardId);
//        List<CommentEntity> commentEntityList = commentRepository.findAllById(Collections.singleton(boardId));
//        System.out.println("commentEntityList = " + commentEntityList);
//        List<CommentDTO> commentDTOList = new ArrayList<>();
//        for (CommentEntity commentEntity : commentEntityList) {
//            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
//        }
//        return commentDTOList;
//    }
}

















