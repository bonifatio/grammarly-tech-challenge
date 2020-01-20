package grammarly.task

import grammarly.task.bktree.{BKTree, Node}

object MainApp {

  def main(args: Array[String]) {

    var dict = Seq("book", "books", "cake", "boo", "cape", "cart", "boon", "cook", "caqes")
    val tr = new BKTree(Node(dict.head))

    //var dict = Seq("Hellow", "funny", "Helo", "Helro")
    //val tr = new BKTree("Hellow")

    for(w <- dict.tail)
      tr.add(w)

    tr.root.dump

    val result = tr.search("caqe", 2, 1)
    println(result)

    //println(tr.search("Hello", 1))
    //println(tr.search("fun", 2))
    //println(tr.search("fun", 1))
  }
}
