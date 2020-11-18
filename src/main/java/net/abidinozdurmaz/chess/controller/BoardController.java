package net.abidinozdurmaz.chess.controller;

import net.abidinozdurmaz.chess.chess.ChessBoard;
import net.abidinozdurmaz.chess.chess.Color;
import net.abidinozdurmaz.chess.chess.Square;
import net.abidinozdurmaz.chess.service.MovePrediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("api")
public class BoardController {

    private ChessBoard chessBoard;
    private Square[][] squareArray;

    @Autowired
    private MovePrediction movePrediction;


    @PostConstruct
    private void postConstruct(){
        chessBoard=new ChessBoard();
        squareArray = chessBoard.startingSequence();
        chessBoard.setSquares(squareArray);
        chessBoard.setMoveOrder(Color.WHITE);
    }

    @GetMapping(produces = "application/json")
    public ChessBoard showChessBoard(){
        return chessBoard;
    }

    @GetMapping(value = "reset",produces = "application/json")
    public void resetBoard(){
        squareArray = chessBoard.startingSequence();
    }

    @PostMapping("/move")
    public ChessBoard move(@RequestBody ChessBoard chessBoard){
        this.chessBoard=movePrediction.changeBoard(chessBoard);
        return movePrediction.changeBoard(chessBoard);

    }
}