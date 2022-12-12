package com.its.board.controller;

import com.its.board.dto.BoardDTO;
import com.its.board.dto.CommentDTO;
import com.its.board.service.BoardService;
import com.its.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final CommentService commentService;

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

    //     /board?page=1
    @GetMapping
//    Pageble 임포트시 반드시 org.springframework.data.domain 로 선택할것!!!!
//    PageableDefault 를 사용했기 때문에 매개변수 없이 기본값을 세팅하여 넘겨줄 수 있음
//    실제로 html(뷰단)에서 넘겨주는 매개변수 없음
    public String paging(@PageableDefault(page = 1) Pageable pageable,
                         Model model) {
        System.out.println("pageable. = " + pageable.getPageNumber());
        Page<BoardDTO> boardDTOList = boardService.paging(pageable);
        model.addAttribute("boardList", boardDTOList);
//        start 페이지 end 페이지 계산방식 -> 삼항연산자 사용
//        int nowPage = boardDTOList.getNumber();
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDTOList.getTotalPages()) ? startPage + blockLimit - 1 : boardDTOList.getTotalPages();
//        삼항연산자 = if/else 를 간단하게 작성한 것, 아래는 풀이를 위한 코드임(기능구현 상관x)
        int test = 10;
        int num = (test > 5) ? test : 100;    // 아래 if문이랑 똑같음
        if (test > 5) {
            num = test;
        } else {
            num = 100;
        }
//        model.addAttribute("pageable", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardPages/paging";
    }


    @GetMapping("/{id}")
    public String findById(@PathVariable Long id,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);

        List<CommentDTO> commentDTOList = commentService.findAll(id);
        if (commentDTOList.size() > 0) {
            model.addAttribute("commentList", commentDTOList);
        } else {
            model.addAttribute("commentList", "empty");
        }
        model.addAttribute("page", page);
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

    @GetMapping("/search")
    public String search(@RequestParam("type") String type,
                         @RequestParam("q") String q,
                         Model model) {
        List<BoardDTO> searchList = boardService.search(type, q);
        model.addAttribute("boardList", searchList);
        return "boardPages/boardList";
    }


}




















