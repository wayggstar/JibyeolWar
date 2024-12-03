package org.wayggstar.jibyeolWar.Ability

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.wayggstar.jibyeolWar.JibyeolWar

class cooldownManager(private val plugin: JibyeolWar) {
    private val cooldowns: MutableMap<String, Long> = mutableMapOf()

    fun isOnCooldown(player: Player, abilityName: String): Boolean {
        val currentTime = System.currentTimeMillis()
        val cooldownEnd = cooldowns[abilityName] ?: 0L
        return currentTime < cooldownEnd
    }

    fun startCooldown(player: Player, abilityName: String, cooldownSeconds: Long) {
        val currentTime = System.currentTimeMillis()
        val cooldownEnd = currentTime + (cooldownSeconds * 1000)

        cooldowns[abilityName] = cooldownEnd

        object : BukkitRunnable() {
            override fun run() {
                cooldowns.remove(abilityName)
                player.sendMessage("§a${abilityName} 능력을 다시 사용할 수 있습니다!")
            }
        }.runTaskLater(plugin, cooldownSeconds * 20)
    }

    fun notifyCooldown(player: Player, abilityName: String) {
        if (isOnCooldown(player, abilityName)) {
            val cooldownEnd = cooldowns[abilityName] ?: 0L
            val remainingTime = (cooldownEnd - System.currentTimeMillis()) / 1000
            player.sendMessage("§c${abilityName} 능력은 아직 쿨타임 중입니다. 남은 시간: $remainingTime 초")
        }
    }
}