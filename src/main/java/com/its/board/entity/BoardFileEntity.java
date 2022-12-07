package com.its.board.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String originalFileName;

    @Column(length = 100)
    private String storedFileName;

//    자식 엔티티에서는 자기를 기준으로 부모 엔티티와 어떤 관계인지 확인
//    게시글(Board) : 첨부파일(BoardFile) = 1:N
//    첨부파일 : 게시글 = N:1
//    N:1 관계에서 사용하는 어노테이션=ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")   // 테이블에 생성될 컬럼 이름=참조키
    private BoardEntity boardEntity;    // 부모(참조할) 엔티티 타입의 필드가 와야함

    public static BoardFileEntity toSaveBoardFileEntity(BoardEntity entity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(entity);
        return boardFileEntity;
    }
}
