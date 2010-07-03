/**
 * PhylipReader writes a list of sequences to a file in Phylip format.
 *
 */

import java.io._
// import org.biojava.bio.alignment._
// import org.biojava.bio.symbol._
// import org.biojava.bio.seq._
import org.biojava.bio.seq.io._
import org.biojavax.bio.phylo.io.phylip._

package bio {

  /** 
   * PhylipReader opens a file and parses the Phylip contents using 
   * an iterator. This implementation is not ready - it may use the
   * BioJava PHYLIPFileListener as below, but one problem is that
   * ID names are restricted to 9 characters.
   *
   * Note: this is a hack.
   */
  class PhylipReader(val filename: String) extends Iterator[Tuple2[String,String]] {
    private lazy val reader = new BufferedReader(new FileReader(filename))

    class PhylipReaderException(string: String) extends Exception(string)

    var id_list : List[String] = Nil
    var seq_list : List[String] = Nil

    object PhylipListener extends PHYLIPFileListener {
      def receiveSequence(s : String) = { seq_list ::= s }
      def setCurrentSequenceName(s : String) = { id_list ::= s }
      def setSitesCount(i: Int) = {}
      def setSequenceCount(i: Int) = {}
      def endFile() = {}
      def startFile() = {}
    }
    lazy val listener = PhylipListener
    private val x = PHYLIPFileFormat.parse(listener,reader)
    val list = id_list.zip(seq_list)
    var pos = 0

    def hasNext() = (pos < list.length)

    def next(): Tuple2[String,String] = {
      val retval = list(pos)
      pos += 1
      retval
    }
  } // PhylipReader

} // bio

