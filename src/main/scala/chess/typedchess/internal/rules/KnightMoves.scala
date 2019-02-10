package chess.typedchess.internal.rules

import chess.typedchess.concrete.TCMove
import chess.typedchess.internal.state.Pieces.Knight
import chess.typedchess.internal.state.{Black, White}

object KnightMoves {

  import chess.typedchess.concrete.Moves._
  import chess.typedchess.concrete.TCTypes._
  import chess.typedchess.internal.state.Board._


  def knightMoves(square: Square, side: Side, squareFree: Square => Boolean): Seq[TCMove] = {
    allKnightMoves(side)(square)
      .filter{ case (k, v) => squareFree(k)}
      .values
      .toSeq
  }

  def knightCaptures(square: Square, side: Side, opponentPieceAt: Square => Boolean): Seq[TCMove] = {
    allKnightMoves(side)(square)
      .filter{ case (k, v) => opponentPieceAt(k)}
      .values
      .toSeq
  }

  private val allKnightSquares: Map[Square, Seq[Square]] = allSquares
    .map { case s@TCSquare(f, r) =>
      val bigLateral = Seq(
        f + 2,
        f - 2
      )
        .flatten
      val smallLateral = Seq(
        f + 1,
        f - 1
      )
        .flatten

      val bigVertical = Seq(
        r + 2,
        r - 2
      )
        .flatten

      val smallVertical = Seq(
        r + 1,
        r - 1
      )
        .flatten

      s -> (bigLateral
        .flatMap { nf =>
          smallVertical
            .map { nr =>
              sq(nf, nr)
            }
        } ++
        smallLateral
          .flatMap { nf =>
            bigVertical
              .map { nr =>
                sq(nf, nr)
              }
          })
    }
    .toMap

  private val allKnightMoves: Map[Side, Map[Square, Map[Square, NonCastle]]] = Map(
    White -> allKnightSquares
      .map { case (from, tos) =>
        from -> tos.map { to =>
          to -> PieceMoveWhite(Knight, from, to)
        }
          .toMap
      },
    Black -> allKnightSquares
      .map { case (from, tos) =>
        from -> tos.map { to =>
          to -> PieceMoveBlack(Knight, from, to)
        }
          .toMap
      }
  )

  private val allKnightCaptures: Map[Side, Map[Square, Map[Square, NonCastle]]] = Map(
    White -> allKnightSquares
      .map { case (from, tos) =>
        from -> tos.map { to =>
          to -> PieceCaptureWhite(Knight, from, to)
        }
          .toMap
      },
    Black -> allKnightSquares
      .map { case (from, tos) =>
        from -> tos.map { to =>
          to -> PieceCaptureBlack(Knight, from, to)
        }
          .toMap
      }
  )
}
