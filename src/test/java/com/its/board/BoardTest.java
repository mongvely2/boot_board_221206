package com.its.board;

import com.its.board.dto.BoardDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.repository.BoardRepository;
import com.its.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

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

}


















