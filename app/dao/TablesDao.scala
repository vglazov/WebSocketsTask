package dao

import models.Table

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

/**
  * Created on 1/14/2017.
  */
trait TablesDao {
  def newTable(table: Table, afterId: Option[Int]): Future[Table]
  def updateTable(table: Table): Future[Unit]
  def removeTable(id: Int): Future[Unit]
  def listAllTables(): Future[Seq[Table]]
}

object TablesDao {
  implicit object InMemoryTablesDao extends TablesDao {

    private val tables: mutable.Map[Int, Table] = mutable.Map()
    private val tablesOrdering: ListBuffer[Int] = ListBuffer()
    private var nextId = 1

    import scala.concurrent.ExecutionContext.Implicits.global

    override def newTable(table: Table, afterId: Option[Int]): Future[Table] = Future {
      val orderingIndex = afterId match {
        case None => tablesOrdering.size
        case Some(-1) => 0
        case Some(thatId) => tablesOrdering.indexOf(thatId) + 1
      }
      if(orderingIndex == -1) throw new IllegalArgumentException(s"Cannot find id in sequence: $afterId")

      val id = generateId()
      val result = Table(Some(id), table.name, table.participants)
      tables(id) = result
      tablesOrdering.insert(orderingIndex, id)
      result
    }

    private def generateId(): Int = {
      val id = nextId
      nextId +=1
      id
    }

    override def updateTable(table: Table): Future[Unit] = Future {
      table.id match {
        case None => throw new IllegalArgumentException("Table id is missing")
        case Some(id) => this.synchronized {
          if(tables.contains(id)) {
            tables(id) = table
          } else {
            throw new IllegalArgumentException(s"Cannot find table by id: $id")
          }
        }
      }
    }

    override def removeTable(id: Int): Future[Unit] = Future {
      if(tables.contains(id) && tablesOrdering.contains(id)) {
        tablesOrdering.remove(tablesOrdering.indexOf(id))
        tables.remove(id)
      } else {
        throw new IllegalArgumentException(s"Cannot find table by id: $id")
      }
    }

    override def listAllTables(): Future[Seq[Table]] = Future(tablesOrdering.map(id => tables(id)))

  }
}