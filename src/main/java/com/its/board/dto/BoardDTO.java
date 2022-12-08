package com.its.board.dto;

import com.its.board.entity.BoardEntity;
import com.its.board.entity.BoardFileEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardTitle;
    private String boardPass;
    private String boardContents;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
    private int boardHits;

//    다중 파일첨부를 위해 List로 선언
//    -> 같은 name 값으로 여러개일 경우 List 타입 지정(체크박스 등)
    private List<MultipartFile> boardFile;
    private List<String> originalFileName;
    private List<String> storedFileName;
    private int fileAttached;

    public static BoardDTO toDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        boardDTO.setBoardHits(boardEntity.getBoardHits());

//        //        파일 관련된 내용 추가
//        if (boardEntity.getFileAttached() == 1) {
////            첨부파일 있음
//            boardDTO.setFileAttached(boardEntity.getFileAttached());    // fileAttached=1
////            첨부파일 이름 가져오기
//            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
//            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());

//        파일 관련된 내용 추가
        if (boardEntity.getFileAttached() == 1) {
//            첨부파일 있음
            boardDTO.setFileAttached(boardEntity.getFileAttached());    // fileAttached=1
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
//            첨부파일 이름 가져오기
            for (BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()) {   //BoardEntity에서 boardFileEntityList 선언해줘서 사용가능함
//                BoardDTO의 originalFileName이 List이기 때문에 add()를 이용하여
//                boardFileEntity에 있는 originalFileName을 옮겨 담음.
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        } else {
//            첨부파일 없음
            boardDTO.setFileAttached(boardEntity.getFileAttached());    // fileAttached=0
        }
        return boardDTO;
    }
}
