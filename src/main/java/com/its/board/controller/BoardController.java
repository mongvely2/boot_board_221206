package com.its.board.controller;

import com.its.board.dto.BoardDTO;
import com.its.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm() {
        return "boardPages/boardSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
//        위 두 줄을 한 줄 코드로 했을 때(해당 코드는 가독성이 떨어짐)
//        model.addAttribute("boardList", boardService.findAll());
        return "boardPages/boardList";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id,
                           Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "boardPages/boardDetail";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteByAxios(@PathVariable Long id) {
        System.out.println("id = " + id);
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public String deleteByGet(@PathVariable Long id) {
        System.out.println("id = " + id);
        boardService.delete(id);
        return "redirect:/board/";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id,
                             Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "boardPages/boardUpdate";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute BoardDTO boardDTO,
                             Model model) {
        boardService.update(boardDTO);
        BoardDTO boardDTO1 = boardService.findById(boardDTO.getId());
        model.addAttribute("a", boardDTO1);
        return "redirect:/board/";
    }

    @PutMapping("/{id}")
    public ResponseEntity updateByAxios(@RequestBody BoardDTO boardDTO) {
//        실행 후 상세조회로 넘어가기 때문에 조회수 하나 중복으로 증가됨
//        메서드 오버로딩 / 생성자 오버로딩 => update 메서드 사용
        boardService.update(boardDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}




















