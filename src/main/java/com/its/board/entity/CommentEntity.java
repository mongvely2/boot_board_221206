package com.its.board.entity;

import com.its.board.dto.CommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column(length = 200, nullable = false)
    private String commentContents;

    @Column
    private LocalDateTime commentCreatedTime;

    @Column
    private LocalDateTime commentUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;


    @ManyToOne(fetch = FetchType.LAZY)      // N:1 = 댓글 : 게시글
    @JoinColumn(name="board_id")    // 테이블에 생성될 컬럼 이름=참조키
    private BoardEntity boardEntity;    // 부모(참조할) 엔티티 타입의 필드가 와야함

//    boardEntity/memberEntity 객체 둘 다를 고려해야함(여기엔 기능적용 안 됨),
    public static CommentEntity toSaveCommentEntity(CommentDTO commentDTO,
                                                    BoardEntity entity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(entity);
        commentEntity.setCommentCreatedTime(commentDTO.getCommentCreatedTime());
        commentEntity.setCommentUpdateTime(commentDTO.getCommentUpdateTime());
        return commentEntity;
    }

}
