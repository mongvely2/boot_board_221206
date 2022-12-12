package com.its.board.service;

import com.its.board.dto.BoardDTO;
import com.its.board.entity.BoardEntity;
import com.its.board.entity.BoardFileEntity;
import com.its.board.entity.MemberEntity;
import com.its.board.repository.BoardFileRepository;
import com.its.board.repository.BoardRepository;
import com.its.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final MemberRepository memberRepository;

    public Long save(BoardDTO boardDTO) throws IOException {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter()).get();

//        if (boardDTO.getBoardFile().isEmpty()) {      // 단수형 파일 업로드: isEmpty 사용
        if (boardDTO.getBoardFile() == null || boardDTO.getBoardFile().size() == 0) {      // 다중 파일 업로드: size 사용
            System.out.println("파일없음");
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO, memberEntity);
            return boardRepository.save(boardEntity).getId();
//            return boardRepository.save(BoardEntity.toSaveEntity(boardDTO)).getId();
        } else {
//            System.out.println("파일있음");
//            MultipartFile boardFile = boardDTO.getBoardFile();
//            String originalFileName = boardFile.getOriginalFilename();
//            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
//            String savePath = "D:\\boot_board_img\\" + storedFileName;
//            boardFile.transferTo(new File(savePath));
//
//            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
//            Long savedId = boardRepository.save(boardEntity).getId();
//
////            JPA는 JOIN 쿼리 없음, 참조키 저장시 객체를 통으로 저장함
////            위에서 리턴받은 id 값으로 해당되는 BoardEntity 전체 데이터 가져오기
//            BoardEntity entity = boardRepository.findById(savedId).get();
////            가져온 전체 데이터 중 필요한 컬럼값 BoardFileEntity에 변환&입력 하기
////            spring JPA는 객체를 통으로 전달(조인)하기 때문에 객체를 통으로 선언하여 가져옴
//            BoardFileEntity boardFileEntity =
//                    BoardFileEntity.toSaveBoardFileEntity(entity, originalFileName, storedFileName);
//            boardFileRepository.save(boardFileEntity).getId();

//          게시글 정보를 먼저 저장하고 해당 게시글의 entity를 가져옴
            System.out.println("파일있음");
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO, memberEntity);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity entity = boardRepository.findById(savedId).get();
//            파일이 담긴 list를 반복문으로 접근하여 하나씩 이름 가져오고, 저장용 이름 만들고
//            로컬 경로에 저장하고 board_file_table에 저장
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
//          MultipartFile boardFile = boardDTO.getBoardFile();  ->  단수일 때 한번만 했던것을 위처럼 반복문으로 진행
                String originalFileName = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String savePath = "D:\\boot_board_img\\" + storedFileName;
                boardFile.transferTo(new File(savePath));
                BoardFileEntity boardFileEntity =
                        BoardFileEntity.toSaveBoardFileEntity(entity, originalFileName, storedFileName);
                boardFileRepository.save(boardFileEntity).getId();
            }
            return savedId;
        }
    }

    @Transactional  // 부모엔티티에서 자식엔티티를 직접 가져올 때 필요한 어노테이션
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdTime"));
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

    public Page<BoardDTO> paging(Pageable pageable) {
//        page: (하단에 표시되는) 해당 page(배열처럼 0번이 1번임) / pageLimit: 보여줄 한 페이지에서의 게시글 수
        int page = pageable.getPageNumber() - 1;
        final int pageLimit = 3;
//        Page<> : 스프링에서 제공하는 인터페이스 / List<> 랑 헷갈리면 안 됨
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
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
        return boardList;
    }

    public List<BoardDTO> search(String type, String q) {
//        작성자 검색
//        select * from board_table where board_writer like '%q%';
        List<BoardDTO> boardDTOList = new ArrayList<>();
        List<BoardEntity> boardEntityList = null;
        if (type.equals("boardWriter")) {
            boardEntityList = boardRepository.findByBoardWriterContainingOrderByIdDesc(q);
        } else if (type.equals("boardTitle")) {
            boardEntityList = boardRepository.findByBoardTitleContainingOrderByIdDesc(q);
        } else {
            boardEntityList = boardRepository.findByBoardTitleContainingOrBoardWriterContainingOrderByIdDesc(q, q);
        }

        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));
        }

        return boardDTOList;
    }
}
