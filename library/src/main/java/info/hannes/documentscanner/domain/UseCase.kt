package info.hannes.documentscanner.domain

import info.hannes.documentscanner.support.Either
import kotlinx.coroutines.*

abstract class UseCase<Type, in Params> where Type : Any? {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = CoroutineScope(Dispatchers.IO).async { run(params) }
        CoroutineScope(Dispatchers.Main).launch { onResult(job.await()) }
    }
}

class Failure(val origin: Throwable)
