package kr.hqservice.teleport.service.impl

import kr.hqservice.teleport.extension.async
import kr.hqservice.teleport.extension.sync
import kr.hqservice.teleport.repository.TeleportRepository
import kr.hqservice.teleport.service.TeleportService
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

class TeleportServiceImpl(
    private val repository: TeleportRepository
) : TeleportService {

    override fun teleport(world: World, player: Player) {
        val maxHeight = world.maxHeight

        val randomX = repository.getRandomX()
        val randomZ = repository.getRandomZ()

        async {
            for (height in maxHeight downTo 62) {
                val floorBlock = world.getBlockAt(randomX, height, randomZ)
                val floorBlockType = floorBlock.type
                val upBlock = floorBlock.getRelative(BlockFace.UP)
                val upBlock2 = upBlock.getRelative(BlockFace.UP)
                if (floorBlockType != Material.AIR && upBlock.type == Material.AIR && upBlock2.type == Material.AIR) {
                    sync {
                        val location = upBlock.location.toCenterLocation()
                        if (floorBlockType.isSolid) {
                            player.teleport(location)
                        } else {
                            player.teleport(location.subtract(0.0, 1.0, 0.0))
                        }
                    }
                    break
                }
            }
        }
    }

    private fun Location.toCenterLocation(): Location {
        return add(0.5, 0.0, 0.5)
    }
}