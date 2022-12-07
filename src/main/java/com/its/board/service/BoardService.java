package com.its.board.service;

import com.its.board.dto.BoardDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.BoardFileEntity;
import com.its.board.repository.BoardFileRepository;
import com.its.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public Long save(BoardDTO boardDTO) throws IOException {
        if (boardDTO.getBoardFile().isEmpty()) {
            System.out.println("파일없음");
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            return boardRepository.save(boardEntity).getId();
//            return boardRepository.save(BoardEntity.toSaveEntity(boardDTO)).getId();
        } else {
            System.out.println("파일있음");
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFileName = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            String savePath = "D:\\boot_board_img\\" + storedFileName;
            boardFile.transferTo(new File(savePath));

            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();

//            JPA는 JOIN 쿼리 없음, 참조키 저장시 객체를 통으로 저장함
//            위에서 리턴받은 id 값으로 해당되는 BoardEntity 전체 데이터 가져오기
            BoardEntity entity = boardRepository.findById(savedId).get();
//            가져온 전체 데이터 중 필요한 컬럼값 BoardFileEntity에 변환&입력 하기
//            spring JPA는 객체를 통으로 전달(조인)하기 때문에 객체를 통으로 선언하여 가져옴
            BoardFileEntity boardFileEntity =
                    BoardFileEntity.toSaveBoardFileEntity(entity, originalFileName, storedFileName);
            boardFileRepository.save(boardFileEntity).getId();
            return savedId;
        }
    }

    @Transactional  // 부모엔티티에서 자식엔티티를 직접 가져올 때 필요한 어노테이션
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));

//            BoardDTO boardDTO = BoardDTO.toDTO(boardEntity);
//            boardDTOList.add(boardDTO);
        }
        return boardDTOList;
    }

    @Transactional  // 부모엔티티에서 자식엔티티를 직접 가져올 때 필요한 어노테이션
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toDTO(boardEntity);
            return boardDTO;
//            return BoardDTO.toDTO(optionalBoardEntity.get());
        } else {
            return null;
        }
    }

    //  쿼리 생성하여 사용시 Transactional 어노테이션 반드시 선언, 안하면 에러
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public void update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
    }
}
