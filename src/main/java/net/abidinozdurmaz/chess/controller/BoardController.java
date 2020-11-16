package net.abidinozdurmaz.chess.controller;

import net.abidinozdurmaz.chess.chess.Square;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class BoardController {

    private Square[][] squareArray;

    public BoardController(){
        squareArray =new Square[8][8];
    }

    @GetMapping(produces = "application/json")
    public Square[][] showBoard(){
        return squareArray;
    }

    @PostMapping("/move")
    public Square[][] setBoard(@RequestBody Square square,int x,int y){

        squareArray[square.getX()][square.getY()]= null;
        square.setX(x);
        square.setY(y);
        squareArray[x][y]=square;
        return squareArray;
    }

    @GetMapping(value = "reset",produces = "application/json")
    public void resetBoard(){
        for (int i = 0; i < squareArray.length; i++) {
            for (int j = 0; j < squareArray.length ; j++) {
                squareArray[i][j]=null;
            }
        }
    }

}
