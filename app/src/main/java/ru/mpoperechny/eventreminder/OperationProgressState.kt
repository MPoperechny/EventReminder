package ru.mpoperechny.eventreminder

@Suppress("DataClassPrivateConstructor")
data class OperationProgressState private constructor(val status: Status, val msg: String? = null) {
    companion object {
        val READY = OperationProgressState(Status.SUCCESS)
        val STARTED = OperationProgressState(Status.RUNNING)
        fun error(msg: String?) = OperationProgressState(Status.FAILED, msg)
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }
}