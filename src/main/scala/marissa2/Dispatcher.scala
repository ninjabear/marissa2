package marissa2

import com.typesafe.scalalogging.Logger

import scala.concurrent.Future

class Dispatcher(group: Seq[(Message)=> Unit]) {

  import scala.concurrent.ExecutionContext.Implicits.global

  def dispatch(m: Message)(implicit logger: Logger): Future[Unit] = {
    val dispatches = group.map {
      f =>
        Future {
          f(m)
        } recoverWith { case e => logger.error("error running route", e); Future.failed(e) }
    }

    Future.sequence(dispatches).map(_=>())
  }

}

object Dispatcher {
  def apply(group: Seq[(Message)=> Unit]): Dispatcher = {
    new Dispatcher(group)
  }
}


