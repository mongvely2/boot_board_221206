package com.its.board;

import com.its.board.dto.BoardDTO;
import com.its.board.dto.CommentDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.CommentEntity;
import com.its.board.repository.BoardRepository;
import com.its.board.repository.CommentRepository;
import com.its.board.service.BoardService;
import com.its.board.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    private BoardDTO newBoard(int i) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardTitle("title:"+i);
        boardDTO.setBoardWriter("writer:"+i);
        boardDTO.setBoardPass("pass:"+i);
        boardDTO.setBoardContents("cotents:"+i);
        return boardDTO;

    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("글작성(save) 테스트")
    public void saveTest() {

        BoardDTO boardDTO = newBoard(1);

//        BoardDTO boardDTO = new BoardDTO();
//        boardDTO.setBoardWriter("조규성");
//        boardDTO.setBoardPass("1234");
//        boardDTO.setBoardTitle("멀티골");
//        boardDTO.setBoardContents("가나전 멀티골");

//        Long savedId = boardService.save(boardDTO);
//
//        BoardDTO savedBoard = boardService.findById(savedId);
//
//        assertThat(boardDTO.getBoardWriter()).isEqualTo(savedBoard.getBoardWriter());
    }

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("글작성 여러개")
//    여러개 데이터가 필요한 테스트 해야 할 때 방법(샘플 이런식으로 하면 된다~)
    public void saveList() throws IOException {
        for (int i=1; i<20; i++) {
            boardService.save(newBoard(i));
        }


//        요즘 쓰는 반복문_ 21~40 사이를 i로 반복문 돌려달라
//        const temp = (id) => {
//            console.log(id);
//        }
//        IntStream.rangeClosed(21, 40).forEach(i -> {
//            boardService.save(newBoard(i));
//        });
    }

    @Test
    @Transactional
    @DisplayName("연관관계 조회 테스트")
    public void findTest() {
//        파일이 첨부된 게시글 조회
        BoardEntity boardEntity = boardRepository.findById(37L).get();
//        첨부파일의 originalFileName 조회
        System.out.println("boardEntity.getBoardFileEntityList() = " + boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
//        native query 에서는 조인 쿼리 사용함
//

    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("댓글작성 테스트")
    public void commentSaveTest() throws IOException {
//        1. 게시글 작성 _ newBoard 라는 게시글 save 메서드 활용
        BoardDTO boardDTO = newBoard(100);
        Long savedId = boardService.save(boardDTO);
//        2. 댓글 작성
        CommentDTO commentDTO = newCommentDTO(savedId, 1);
        Long commentSavedId = commentService.save(commentDTO);
//        3. 저장된 댓글 아이디로 댓글 조회
        CommentEntity commentEntity = commentRepository.findById(commentSavedId).get();
//        4. 작성자 값이 일치하는지 확인
        assertThat(commentDTO.getCommentWriter()).isEqualTo(commentEntity.getCommentWriter());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("댓글 목록 테스트")
    public void commentListTest() throws IOException {
//        1. 게시글 작성
        BoardDTO boardDTO = newBoard(3);
        Long savedId = boardService.save(boardDTO);
//        2. 해당 게시글에 댓글 3개 작성
        IntStream.rangeClosed(1, 3).forEach(i -> {      // 반복문
        CommentDTO commentDTO = newCommentDTO(savedId, i);
        commentService.save(commentDTO);

        });
//        CommentDTO commentDTO = newCommentDTO(savedId, 3);
//        commentService.save(commentDTO);
//        commentService.save(commentDTO);
//        commentService.save(commentDTO);
//        3. 댓글 목록 조회했을 때 목록 갯수가 3이면 테스트 통과(댓글목록==3)
        List<CommentDTO> commentDTOList = commentService.findAll(savedId);
        assertThat(commentDTOList.size()).isEqualTo(3);
//        BoardEntity boardEntity = commentRepository.findById(commentDTO.getBoardId()).get();
//        commentRepository.findAllByBoardEntityOrderByIdDesc(commentSaved1)
    }

    private CommentDTO newCommentDTO(Long boardId, int i) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentWriter("commentWriter" + i);
        commentDTO.setCommentContents("commentContents" + i);
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("페이징 객체 확인")
    public void pagingParams() {
        int page = 4;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id")));
        // Page 객체가 제공해주는 메서드 확인
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수( = pageLimit)
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부

//        컨트롤러 이동을 위해 Page<BoardEntity> -> Page<BoardDTO>
        Page<BoardDTO> boardList = boardEntities.map(
//                boardEntities에 담긴 boardEntity 객체를 board에 담아서
//                boardDTO 객체로 하나씩 옮겨 담는 과정
                board -> new BoardDTO(board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle(),
                        board.getCreatedTime(),
                        board.getBoardHits()
                        )
        );
        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부


    }

}


















