package grammarly.task.bktree

import scala.annotation.tailrec
import BKTree._

class BKTree(
    val root: Node,
    distance: (String, String) => Int = (levenshteinDistance _)) {

  def add(word: String): Unit = {

    @tailrec
    def findNodeToAppendChild(cNode: Node): Unit = {
      val dist = distance(cNode.word, word)

      if (cNode.containsKey(dist)) {
        if (dist != 0) findNodeToAppendChild(cNode.getChild(dist))
      }
      else {
        cNode.addChild(dist, word)
      }
    }

    findNodeToAppendChild(root)
  }

  def search(word: String, d: Int, dMin: Int): Seq[DictionaryMatch] = {

    @tailrec
    def loop(nodes: Vector[Node], acc: Seq[DictionaryMatch]): Seq[DictionaryMatch] = nodes match {
      case Vector() => acc

      case h +: tail =>

        val curDist = distance(h.word, word)
        val newAcc = if (curDist <= d) (h.word -> curDist) +: acc else acc

        val minDist = math.max(curDist - d, 0)
        val maxDist = curDist + d

        val distancesToScan = h.keys.filter(key => (key >= minDist) && (key <= maxDist))

        val newNodes = distancesToScan map { h.getChild }

        loop(newNodes ++ tail, newAcc)
    }

    loop(Vector(root), Nil)
  }

}

  object BKTree {

    type DictionaryMatch = (String, Int)

    def levenshteinDistance(s: String, t: String): Int = {

      // degenerate cases
      if (s == t) 0
      else if (s.length == 0) t.length
      else if (t.length == 0) s.length
      else {

        // create two work vectors of integer distances
        val v0 = new Array[Int](t.length + 1)
        val v1 = new Array[Int](t.length + 1)

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (i <- 0 until v0.length) v0(i) = i

        for (i <- 0 until s.length)
        {
          // calculate v1 (current row distances) from the previous row v0

          // first element of v1 is A[i+1][0]
          //   edit distance is delete (i+1) chars from s to match empty t
          v1(0) = i + 1

          // use formula to fill in the rest of the row
          for (j <- 0 until t.length)
          {
            val cost = if(s(i) == t(j)) 0 else 1
            v1(j + 1) = math.min(math.min(v1(j) + 1, v0(j + 1) +1), v0(j) + cost)
          }

          // copy v1 (current row) to v0 (previous row) for next iteration
          for (j <- 0 until v0.length) v0(j) = v1(j)
        }

        v1(t.length)
      }
  }
}
