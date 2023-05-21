package kr.hqservice.teleport.extension

import kr.hqservice.teleport.HQRandomTeleport.Companion.plugin
import org.bukkit.scheduler.BukkitTask

private val scheduler get() = plugin.server.scheduler

fun sync(block: () -> Unit) {
    scheduler.runTask(plugin, Runnable(block))
}

fun async(block: () -> Unit) {
    scheduler.runTaskAsynchronously(plugin, Runnable(block))
}

fun later(delay: Int = 1, async: Boolean = false, block: () -> Unit = {}): BukkitTask {
    return if (async) {
        scheduler.runTaskLaterAsynchronously(plugin, Runnable(block), delay.toLong())
    } else {
        scheduler.runTaskLater(plugin, Runnable(block), delay.toLong())
    }
}