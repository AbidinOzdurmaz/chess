package net.abidinozdurmaz.chess.service;

import net.abidinozdurmaz.chess.chess.ChessBoard;
import net.abidinozdurmaz.chess.chess.Color;
import net.abidinozdurmaz.chess.chess.Piece;
import net.abidinozdurmaz.chess.chess.Square;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ChessEngine {

    Random r = new Random();
    List<Square[][]> possibleBoards;
    ChessBoard cBoard;

    public ChessBoard move(ChessBoard chessBoard) {

        cBoard = chessBoard;
        possibleBoards = new ArrayList<>();


        //burada eğer taşımız siyahsa simetri alıyoruz.
        if (chessBoard.getMoveOrder() == Color.BLACK) {
            chessBoard.setSquares(spinBoard(chessBoard.getSquares()));
        }

        for (int i = 0; i < chessBoard.getSquares().length; i++) {
            for (int j = 0; j < chessBoard.getSquares().length; j++) {

                if (chessBoard.getSquares()[i][j] != null) {

                    if (chessBoard.getMoveOrder() == chessBoard.getSquares()[i][j].getColor()) {

                        switch (chessBoard.getSquares()[i][j].getPiece()) {
                            case PAWN: {
                                possibleBoards.addAll(calculatePawnMoves(chessBoard, i, j));
                                break;
                            }
                        }
                    }
                }
            }
        }
        chessBoard.setSquares(guess(possibleBoards));
        return chessBoard;
    }

    private List<Square[][]> calculatePawnMoves(ChessBoard chessBoard, int i, int j) {

        List<Square[][]> pawnPossibleBoards = new ArrayList<>();

        if (j != 7) {

            //piyon 2 adım ilerleme
            if (j == 1 && chessBoard.getSquares()[i][j + 2] == null && chessBoard.getSquares()[i][j + 1] == null) {

                Square[][] squares = chessBoard.cloneBoard();

                squares[i][j + 2] = chessBoard.getSquares()[i][j];
                squares[i][j] = null;
                pawnPossibleBoards.add(squares);
            }

            //piyon sağ çapraza yeme i = 7 olursa arrayden dışarı çıkar
            //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
            if (i != 7 && chessBoard.getSquares()[i + 1][j + 1] != null && chessBoard.getSquares()[i + 1][j + 1].getColor() != chessBoard.getMoveOrder()) {

                Square[][] squares = chessBoard.cloneBoard();

                //vezir olma durumu kontrol ediliyor
                if (j == 6) {
                    squares[i + 1][j + 1] = new Square(Piece.QUEEN, chessBoard.getMoveOrder());
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                } else {
                    squares[i + 1][j + 1] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                }
            }
            //piyon sol çapraza yeme i = 0 olursa arrayden dışarı çıkar
            //çaprazında bir taş varsa yiyebilir ve o taş rakibinse
            if (i != 0 && chessBoard.getSquares()[i - 1][j + 1] != null && chessBoard.getSquares()[i - 1][j + 1].getColor() != chessBoard.getMoveOrder()) {

                Square[][] squares = chessBoard.cloneBoard();

                //vezir olma durumu kontrol ediliyor
                if (j == 6) {
                    squares[i - 1][j + 1] = new Square(Piece.QUEEN, chessBoard.getMoveOrder());
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                } else {
                    squares[i - 1][j + 1] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                }
            }

            //eğer önü boş ise gidebilir

            if (chessBoard.getSquares()[i][j + 1] == null) {

                Square[][] squares = chessBoard.cloneBoard();

                //eğer son kareye gelmişse vezire dönüşebilir
                if (j == 6) {
                    squares[i][j + 1] = new Square(Piece.QUEEN, chessBoard.getMoveOrder());
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                } else {
                    squares[i][j + 1] = chessBoard.getSquares()[i][j];
                    squares[i][j] = null;
                    pawnPossibleBoards.add(squares);
                }
            }
        }

        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(pawnPossibleBoards);
        }
        return pawnPossibleBoards;
    }

    private Square[][] guess(List<Square[][]> possibleBoards) {
        if (possibleBoards.size() > 0) {
            int index = r.nextInt(possibleBoards.size());
            System.out.println("index değeri " + index);
            System.out.println("Döndüreceği veri : " + possibleBoards.get(index));
            return possibleBoards.get(index);
        }
        return null;
    }

    private List<Square[][]> spinBoard(List<Square[][]> squares) {

        List<Square[][]> spinBoardList = new ArrayList<>();

        for (Square[][] square : squares) {

            Square[][] sq = new Square[8][8];

            for (int i = 0; i < sq.length; i++) {
                for (int j = 0; j < 8; j++) {
                    sq[i][j] = square[7 - i][7 - j];
                }
            }
            spinBoardList.add(sq);
        }
        return spinBoardList;
    }

    private Square[][] spinBoard(Square[][] squares) {

        Square[][] sq = new Square[8][8];

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                sq[i][j] = squares[7 - i][7 - j];
            }
        }
        return sq;
    }

    private void movePiece(Square[][] squares, int startX, int startY, int endX, int endY) {
        squares[endX][endY] = cBoard.getSquares()[startX][startX];
        squares[startX][startY] = null;
    }

}
