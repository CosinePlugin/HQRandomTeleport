package kr.hqservice.teleport

import kr.hqservice.teleport.command.TeleportCommand
import kr.hqservice.teleport.listener.BoatListener
import kr.hqservice.teleport.repository.TeleportRepository
import kr.hqservice.teleport.service.TeleportService
import kr.hqservice.teleport.service.impl.TeleportServiceImpl
import kr.ms.core.bstats.Metrics
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class HQRandomTeleport : JavaPlugin(), Listener {

    companion object {
        internal lateinit var plugin: JavaPlugin
            private set

        internal const val prefix = "§b[ HQRandomTeleport ]§f"
    }

    override fun onLoad() {
        plugin = this
    }

    lateinit var teleportRepository: TeleportRepository
        private set

    lateinit var teleportService: TeleportService
        private set

    override fun onEnable() {
        if (server.pluginManager.getPlugin("MS-Core") == null) {
            logger.warning("MS-Core 플러그인을 찾을 수 없어, 플러그인이 비활성화됩니다.")
            server.pluginManager.disablePlugin(this)
            return
        }
        Metrics(this, 18264)

        teleportRepository = TeleportRepository(this)
        teleportRepository.load()

        teleportService = TeleportServiceImpl(teleportRepository)

        server.pluginManager.registerEvents(BoatListener(), this)

        getCommand("랜덤")?.executor = TeleportCommand(this)
    }

    override fun onDisable() {

    }
}