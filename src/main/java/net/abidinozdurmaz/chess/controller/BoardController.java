package net.abidinozdurmaz.chess.controller;

import net.abidinozdurmaz.chess.chess.ChessBoard;
import net.abidinozdurmaz.chess.service.ChessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class BoardController {



    private final ChessEngine chessEngine;

    @Autowired
    public BoardController(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    @PostMapping("/move")
    public ChessBoard move(@RequestBody ChessBoard chessBoard){

        return chessEngine.move(chessBoard);

    }
}