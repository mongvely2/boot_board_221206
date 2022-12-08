package com.its.board.entity;

import com.its.board.dto.CommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)      // N:1 = 댓글 : 게시글
    @JoinColumn(name="board_id")    // 테이블에 생성될 컬럼 이름=참조키
    private BoardEntity boardEntity;    // 부모(참조할) 엔티티 타입의 필드가 와야함

    public static CommentEntity toSaveCommentEntity(CommentDTO commentDTO,
                                                    BoardEntity entity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(entity);
        return commentEntity;
    }
}
