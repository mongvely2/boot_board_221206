package com.its.board.controller;

import com.its.board.dto.CommentDTO;
import com.its.board.service.BoardService;
import com.its.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

//    @PostMapping("/save")
//    public @ResponseBody List<CommentDTO> commentSave(@ModelAttribute CommentDTO commentDTO) {
//        commentService.commentSave(commentDTO);
//        System.out.println("controller commentDTO = " + commentDTO);
//        List<CommentDTO> result = commentService.findAll(commentDTO.getBoardId());
//        System.out.println("controller result = " + result);
//        return result;
//    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody CommentDTO commentDTO) {
        commentService.save(commentDTO);
        List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }



}
