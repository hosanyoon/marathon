package com.ssafy.marathon.service.user;

import com.ssafy.marathon.db.entity.board.Board;
import com.ssafy.marathon.db.repository.BoardRepository;
import com.ssafy.marathon.dto.response.board.BoardResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public BoardResDto getDetailBoard(Long boardSeq) {
        Optional<Board> findBoard = boardRepository.findById(boardSeq);
        Board board = findBoard.orElseThrow();
        board.addViewCnt();

        return BoardResDto.builder()
            .boardSeq(board.getSeq())
            .title(board.getTitle())
            .content(board.getContent())
            .registDate(board.getRegistDate())
            .viewCnt(board.getViewCnt())
            .build();
    }

    @Transactional(readOnly = true)
    public Page<BoardResDto> getBoardPages(int pageNum, String contentCondition, String titleCondition) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);

        Page<BoardResDto> boardResDtoPages;

        if (contentCondition.isBlank() && titleCondition.isBlank()) {
            boardResDtoPages = boardRepository.findAllByOrderByRegistDateDesc(pageRequest)
                .map(board -> BoardResDto.builder()
                    .boardSeq(board.getSeq())
                    .title(board.getTitle())
                    .registDate(board.getRegistDate())
                    .viewCnt(board.getViewCnt())
                    .build());
        } else if (!contentCondition.isBlank() && !titleCondition.isBlank()) {
            boardResDtoPages = boardRepository.findAllByContentContainingOrTitleContainingOrderByRegistDateDesc(
                    contentCondition, titleCondition,
                    pageRequest)
                .map(board -> BoardResDto.builder()
                    .boardSeq(board.getSeq())
                    .title(board.getTitle())
                    .registDate(board.getRegistDate())
                    .viewCnt(board.getViewCnt())
                    .build());
        } else if (!titleCondition.isBlank()) {
            boardResDtoPages = boardRepository.findAllByTitleContainingOrderByRegistDateDesc(
                    titleCondition, pageRequest)
                .map(board -> BoardResDto.builder()
                    .boardSeq(board.getSeq())
                    .title(board.getTitle())
                    .registDate(board.getRegistDate())
                    .viewCnt(board.getViewCnt())
                    .build());
        } else {
            boardResDtoPages = boardRepository.findAllByContentContainingOrderByRegistDateDesc(
                    contentCondition, pageRequest)
                .map(board -> BoardResDto.builder()
                    .boardSeq(board.getSeq())
                    .title(board.getTitle())
                    .registDate(board.getRegistDate())
                    .viewCnt(board.getViewCnt())
                    .build());
        }

        return boardResDtoPages;
    }
}