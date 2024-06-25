package org.example.chess.board;

import static org.example.utils.StringUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.chess.pieces.Piece;
import org.example.chess.pieces.Piece.Color;
import org.example.chess.pieces.Piece.PieceFactory;
import org.example.chess.pieces.Piece.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void create() throws Exception {
        board.initialize();
        assertEquals(32, board.pieceCount());
        String blankRank = appendNewLine("........");
        assertEquals(
                appendNewLine("RNBQKBNR") +
                        appendNewLine("PPPPPPPP") +
                        blankRank + blankRank + blankRank + blankRank +
                        appendNewLine("pppppppp") +
                        appendNewLine("rnbqkbnr"),
                board.showBoard()
        );
    }

    @Test
    void testCountPiecesByColorAndType() {
        board.initialize();
        assertEquals(8, board.countPiecesByColorAndType(Color.BLACK, Type.PAWN));
        assertEquals(2, board.countPiecesByColorAndType(Color.BLACK, Type.BISHOP));
        assertEquals(2, board.countPiecesByColorAndType(Color.BLACK, Type.KNIGHT));
        assertEquals(2, board.countPiecesByColorAndType(Color.BLACK, Type.ROOK));
        assertEquals(1, board.countPiecesByColorAndType(Color.BLACK, Type.KING));
        assertEquals(1, board.countPiecesByColorAndType(Color.BLACK, Type.QUEEN));

        assertEquals(8, board.countPiecesByColorAndType(Color.WHITE, Type.PAWN));
        assertEquals(2, board.countPiecesByColorAndType(Color.WHITE, Type.BISHOP));
        assertEquals(2, board.countPiecesByColorAndType(Color.WHITE, Type.KNIGHT));
        assertEquals(2, board.countPiecesByColorAndType(Color.WHITE, Type.ROOK));
        assertEquals(1, board.countPiecesByColorAndType(Color.WHITE, Type.KING));
        assertEquals(1, board.countPiecesByColorAndType(Color.WHITE, Type.QUEEN));
    }

    @Test
    void findPiece() throws Exception {
        board.initialize();
        System.out.println(board.findPiece("a8"));

        assertEquals(PieceFactory.createBlackRook(), board.findPiece("a8"));
        assertEquals(PieceFactory.createBlackRook(), board.findPiece("h8"));
        assertEquals(PieceFactory.createWhiteRook(), board.findPiece("a1"));
        assertEquals(PieceFactory.createWhiteRook(), board.findPiece("h1"));
    }

    @Test
    void move() throws Exception {
        board.initializeEmpty();

        String position = "b5";
        Piece piece = PieceFactory.createBlackRook();
        board.move(position, piece);

        assertEquals(piece, board.findPiece(position));
        System.out.println(board.showBoard());
    }
}