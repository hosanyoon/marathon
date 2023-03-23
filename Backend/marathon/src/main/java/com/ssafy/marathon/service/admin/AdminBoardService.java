package com.ssafy.marathon.service.admin;

import com.ssafy.marathon.db.entity.board.Board;
import com.ssafy.marathon.db.repository.BoardRepository;
import com.ssafy.marathon.dto.request.board.BoardReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminBoardService {
    private final BoardRepository boardRepository;

    public void writeBoard(BoardReqDto boardReqDto) {
        Board board = Board.builder()
            .title(boardReqDto.getTitle())
            .content(boardReqDto.getContent())
            .registDate(LocalDateTime.now())
            .viewCnt(0)
            .build();
        boardRepository.save(board);
    }

    public void updateBoard(Long boardSeq, BoardReqDto boardReqDto) {
        Optional<Board> findBoard = boardRepository.findById(boardSeq);

        Board board = findBoard.orElseThrow();
        board.updateBoard(boardReqDto.getContent(), boardReqDto.getTitle());
    }

    public void deleteBoard(Long boardSeq) {
        Optional<Board> findBoard = boardRepository.findById(boardSeq);
        Board board = findBoard.orElseThrow();

        boardRepository.delete(board);
    }
}