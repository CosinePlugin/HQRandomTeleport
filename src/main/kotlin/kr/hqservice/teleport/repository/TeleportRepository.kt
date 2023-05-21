package kr.hqservice.teleport.repository

import kr.hqservice.teleport.HQRandomTeleport
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class TeleportRepository(
    plugin: HQRandomTeleport
) {

    private companion object {
        const val path = "config.yml"
    }

    private var file: File
    private var config: YamlConfiguration

    init {
        val newFile = File(plugin.dataFolder, path)
        if (!newFile.exists() && plugin.getResource(path) != null) {
            plugin.saveResource(path, false)
        }
        file = newFile
        config = YamlConfiguration.loadConfiguration(file)
    }

    private var useBoat = false

    private var xRange = -5000..5000
    private var zRange = -5000..5000

    fun load() {
        useBoat = config.getBoolean("use-boat")
        config.getConfigurationSection("limit")?.apply {
            xRange = getConfigurationSection("x").getRange()
            zRange = getConfigurationSection("z").getRange()
        }
    }

    fun reload() {
        config.load(file)
        load()
    }

    private fun ConfigurationSection.getRange(): IntRange {
        return getInt("min")..getInt("max")
    }

    fun getRandomX() = xRange.random()

    fun getRandomZ() = zRange.random()

    fun isBoatEnable() = useBoat
}