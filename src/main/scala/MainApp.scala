import scala.collection.immutable.HashMap
import scala.annotation.tailrec

class Node(val word: String)
{
  private var children: HashMap[Int, Node] = HashMap()

  def getChild(i: Int) = children(i)

  def keys = children.keys

  def containsKey(key: Int) = children.contains(key)

  def addChild(key: Int, word: String) = children += (key -> Node(word))
}

object Node {
  def apply(word: String): Node = new Node(word)
}

class BKTree() {

  private var root: Node = null

  def isEmpty = (root == null)

  def add(word: String) = {

    @tailrec
    def findNodeToAppendChild(cNode: Node): Option[(Node, Int)] = {
      val dist = levenshteinDistance(cNode.word, word)

      if(cNode.containsKey(dist)) {
        if(dist == 0) None else findNodeToAppendChild(cNode.getChild(dist))
      }
      else Some((cNode, dist))
    }

    if(isEmpty) {
      root = Node(word)
    }
    else {
      findNodeToAppendChild(root) foreach { case (nd, dist) => nd.addChild(dist, word) }
    }
  }

  def search(word: String, d: Int, dMin: Int) = {
    if(isEmpty) Seq()
    else recursiveSearch(Seq(root), word, d, dMin, Seq())
  }

  @tailrec
  private def recursiveSearch(nodes: Seq[Node], word: String, d: Int,
                              dMin: Int, acc: Seq[(String, Int)]): Seq[(String, Int)] = nodes match {
    case Nil => acc

    case h +: tail =>

      val curDist = levenshteinDistance(h.word, word)
      val newAcc = if (curDist <= d) (h.word -> curDist) +: acc else acc

      val minDist = math.max(curDist - d, 0)
      val maxDist = curDist + d

      val distancesToScan = h.keys.filter(key => (key >= minDist) && (key <= maxDist)).toVector

      val newNodes = distancesToScan map { h.getChild(_) }
      recursiveSearch(newNodes ++ tail, word, d, dMin, newAcc)
  }

  def levenshteinDistance(s: String, t: String):Int = {

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

object MainApp {

  def main(args: Array[String]) {

    var dict = Seq("book", "books", "cake", "boo", "cape", "cart", "boon", "cook", "caqes")
    val tr = new BKTree()

    //var dict = Seq("Hellow", "funny", "Helo", "Helro")
    //val tr = new BKTree("Hellow")

    for(w <- dict) {
      tr.add(w)
    }

    val result = tr.search("caqe", 2, 1)
    println(result)

    //println(tr.search("Hello", 1))
    //println(tr.search("fun", 2))
    //println(tr.search("fun", 1))
  }
}
