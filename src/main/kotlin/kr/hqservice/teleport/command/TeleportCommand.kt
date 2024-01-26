package kr.hqservice.teleport.command

import kr.hqservice.teleport.HQRandomTeleport
import kr.hqservice.teleport.HQRandomTeleport.Companion.prefix
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportCommand(
    plugin: HQRandomTeleport
) : CommandExecutor {

    private val teleportRepository = plugin.teleportRepository
    private val teleportService = plugin.teleportService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            printHelp(sender)
            return true
        }
        checker(sender, args)
        return true
    }

    private fun printHelp(player: CommandSender) {
        player.sendMessage("$prefix 랜덤 티피 명령어 도움말")
        player.sendMessage("")
        player.sendMessage("$prefix /랜덤 이동 [월드] [유저]")
        player.sendMessage("$prefix /랜덤 리로드")
    }

    private fun checker(sender: CommandSender, args: Array<out String>) {
        when (args[0]) {
            "이동" -> {
                if (sender !is Player) {
                    sender.sendMessage("$prefix 콘솔에서 사용할 수 없는 명령어입니다.")
                    return
                }
                if (args.size == 1) {
                    sender.sendMessage("$prefix 월드를 입력해주세요.")
                    return
                }
                val server = sender.server
                val worldName = args[1]
                val world = server.getWorld(worldName)
                if (world == null) {
                    sender.sendMessage("$prefix 존재하지 않는 월드입니다.")
                    return
                }
                if (args.size == 2) {
                    sender.sendMessage("$prefix 닉네임을 입력해주세요.")
                    return
                }
                val targetName = args[2]
                val target = server.getPlayer(targetName) ?: run {
                    sender.sendMessage("$prefix 존재하지 않는 유저입니다.")
                    return
                }
                teleportService.teleport(world, target)
            }
            "리로드" -> reload(sender)
        }
    }

    private fun reload(sender: CommandSender) {
        teleportRepository.reload()
        sender.sendMessage("$prefix config.yml이 리로드되었습니다.")
    }
}