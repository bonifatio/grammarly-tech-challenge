package grammarly.task.bktree

import scala.collection.mutable

class Node(val word: String)
{
  private val children: mutable.HashMap[Int, Node] = mutable.HashMap()

  def getChild(i: Int): Node = children(i)

  def keys: Vector[Int] = children.keys.toVector

  def containsKey(key: Int): Boolean = children.contains(key)

  def addChild(key: Int, word: String): Unit = children.update(key, Node(word))

  private def loopDump(n: Node, level: Int, d: Int): Unit = {

    val indentedRow =
      if(d == 0 || level == 0) {
        n.word
      } else {
        if(level == 1)
          s"├──${n.word} @ $d"
        else
          s"│${"  " * (level-1)}└─${n.word} @ $d"
      }

    println(indentedRow)
    
    n.children.zipWithIndex foreach {
      case ((k, v), i) =>
        loopDump(v, level+1, k)
    }
  }

  def dump: Unit = {
    loopDump(this, level = 0, d = 0)
  }
}

object Node {
  def apply(word: String): Node = new Node(word)
}
