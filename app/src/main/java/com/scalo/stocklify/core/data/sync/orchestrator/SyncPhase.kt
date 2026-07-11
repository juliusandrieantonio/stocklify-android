package com.scalo.stocklify.core.data.sync.orchestrator

import com.scalo.stocklify.core.data.sync.enums.SyncTask

/** A named phase of the pull: the task shown in progress UI, plus every step that belongs to it. */
class SyncPhase(val task: SyncTask, val steps: List<ScopedStep<*>>)

fun phase(task: SyncTask, vararg steps: ScopedStep<*>) = SyncPhase(task, steps.toList())