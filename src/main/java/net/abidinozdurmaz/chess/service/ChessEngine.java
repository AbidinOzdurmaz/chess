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

    private Random r = new Random();
    private List<Square[][]> possibleBoards;
    private ChessBoard chessBoard;

    public ChessBoard move(ChessBoard chessBoard) {


        //burada eğer taşımız siyahsa simetri alıyoruz.
        if (chessBoard.getMoveOrder() == Color.BLACK) {
            chessBoard.setSquares(spinBoard(chessBoard.getSquares()));
        }
        possibleBoards = new ArrayList<>();
        this.chessBoard = chessBoard;


        for (int i = 0; i < chessBoard.getSquares().length; i++) {
            for (int j = 0; j < chessBoard.getSquares().length; j++) {

                if (chessBoard.getSquares()[i][j] != null) {

                    if (chessBoard.getMoveOrder() == chessBoard.getSquares()[i][j].getColor()) {

                        switch (chessBoard.getSquares()[i][j].getPiece()) {
                            case PAWN: {
                                possibleBoards.addAll(calculatePawnMoves(i, j));
                                break;
                            }
                            case KING: {
                                possibleBoards.addAll(calculateKingMoves(i, j));
                                break;
                            }
                            case KNIGHT: {
                                possibleBoards.addAll(calculateKnightMoves(i, j));
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

    private List<Square[][]> calculatePawnMoves(int i, int j) {

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

    private List<Square[][]> calculateKingMoves(int i, int j) {
        List<Square[][]> kingPossibleBoards = new ArrayList<>();

        //şah bir hamle öne gitme
        //taş yeme durumu kontrol ediliyor
        if (j != 7 &&
                (chessBoard.getSquares()[i][j + 1] == null ||
                        chessBoard.getSquares()[i][j + 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah bir hamle geriye gitme
        //taş yeme durumu kontrol ediliyor
        if (j != 0 &&
                (chessBoard.getSquares()[i][j - 1] == null ||
                        chessBoard.getSquares()[i][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah bir hamle sola gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 0 &&
                (chessBoard.getSquares()[i - 1][j] == null ||
                        chessBoard.getSquares()[i - 1][j].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah bir hamle sağa gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 7 &&
                (chessBoard.getSquares()[i + 1][j] == null ||
                        chessBoard.getSquares()[i + 1][j].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }

        //şah sağ üst çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 7 && j != 7 &&
                (chessBoard.getSquares()[i + 1][j + 1] == null ||
                        chessBoard.getSquares()[i + 1][j + 1].getColor() != chessBoard.getMoveOrder())) {
            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah sol üst çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 0 && j != 7 &&
                (chessBoard.getSquares()[i - 1][j + 1] == null ||
                        chessBoard.getSquares()[i - 1][j + 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah sağ alt çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 7 && j != 0 &&
                (chessBoard.getSquares()[i + 1][j - 1] == null ||
                        chessBoard.getSquares()[i + 1][j - 1].getColor() != chessBoard.getMoveOrder())) {
            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }
        //şah sol alt çapraz gitme
        //taş yeme durumu kontrol ediliyor
        if (i != 0 && j != 0 &&
                (chessBoard.getSquares()[i - 1][j - 1] == null ||
                        chessBoard.getSquares()[i - 1][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            kingPossibleBoards.add(squares);
        }

        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(kingPossibleBoards);
        }
        return kingPossibleBoards;
    }

    private List<Square[][]> calculateKnightMoves(int i, int j) {

        List<Square[][]> knightPossibleBoards = new ArrayList<>();

        //at 2 sağa 1 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i < 6 && j != 7 &&
                (chessBoard.getSquares()[i + 2][j + 1] == null ||
                        chessBoard.getSquares()[i + 2][j + 1].getColor() != chessBoard.getMoveOrder())) {
            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 2][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 2 sağa 1 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i < 6 && j != 0 &&
                (chessBoard.getSquares()[i + 2][j - 1] == null ||
                        chessBoard.getSquares()[i + 2][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 2][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 2 sola 1 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i > 1 && j != 7 &&
                (chessBoard.getSquares()[i - 2][j + 1] == null ||
                        chessBoard.getSquares()[i - 2][j + 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 2][j + 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 2 sola 1 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i > 1 && j != 0 &&
                (chessBoard.getSquares()[i - 2][j - 1] == null ||
                        chessBoard.getSquares()[i - 2][j - 1].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 2][j - 1] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 1 sağa 2 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 7 && j < 6 &&
                (chessBoard.getSquares()[i + 1][j + 2] == null ||
                        chessBoard.getSquares()[i + 1][j + 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j + 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);

        }
        //at 1 sola 2 yukarı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 0 && j < 6 &&
                (chessBoard.getSquares()[i - 1][j + 2] == null ||
                        chessBoard.getSquares()[i - 1][j + 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j + 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 1 sağa 2 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 7 && j > 1 &&
                (chessBoard.getSquares()[i + 1][j - 2] == null ||
                        chessBoard.getSquares()[i + 1][j - 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i + 1][j - 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        //at 1 sola 2 aşağı hareketi
        //burada null kontrolü yapmamızın nedeni bir sonraki kontrol de null pointer exception almamak için
        //bir sonraki kontrol ise eğer konumda kendi taşı varsa yememesi için yapılmıştır
        if (i != 0 && j > 1 &&
                (chessBoard.getSquares()[i - 1][j - 2] == null ||
                        chessBoard.getSquares()[i - 1][j - 2].getColor() != chessBoard.getMoveOrder())) {

            Square[][] squares = chessBoard.cloneBoard();
            squares[i - 1][j - 2] = chessBoard.getSquares()[i][j];
            squares[i][j] = null;
            knightPossibleBoards.add(squares);
        }

        if (chessBoard.getMoveOrder() == Color.BLACK) {
            return spinBoard(knightPossibleBoards);
        }
        return knightPossibleBoards;
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
        squares[endX][endY] = this.chessBoard.getSquares()[startX][startX];
        squares[startX][startY] = null;
    }

}
