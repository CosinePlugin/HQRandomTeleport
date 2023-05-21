package kr.hqservice.teleport.service.impl

import kr.hqservice.teleport.extension.async
import kr.hqservice.teleport.extension.sync
import kr.hqservice.teleport.repository.TeleportRepository
import kr.hqservice.teleport.service.TeleportService
import org.bukkit.Location
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

                if (!floorBlockType.isSolid) continue

                val upBlock = floorBlock.getRelative(BlockFace.UP)
                val upBlockType = upBlock.type

                val upBlock2 = upBlock.getRelative(BlockFace.UP)
                val upBlockType2 = upBlock2.type

                if (floorBlockType.isSolid && !upBlockType.isSolid && !upBlockType2.isSolid) {
                    sync { player.teleport(upBlock.location.toCenterLocation()) }
                    break
                }
            }
        }
    }

    private fun Location.toCenterLocation(): Location {
        return add(0.5, 0.0, 0.5)
    }
}