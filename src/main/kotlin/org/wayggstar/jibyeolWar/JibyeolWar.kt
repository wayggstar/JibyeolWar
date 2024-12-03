package org.wayggstar.jibyeolWar

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.wayggstar.jibyeolWar.Ability.Abilities.Thor
import org.wayggstar.jibyeolWar.Ability.Ability
import java.io.File

class JibyeolWar : JavaPlugin() {

    companion object {
        lateinit var instance: JibyeolWar
            private set
    }

    private lateinit var gameManager: GameManager

    private val abilities: List<Ability> = listOf(
        Thor()
    )

    override fun onEnable() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        instance = this
        gameManager = GameManager(this)

        val abilitiesFile = File(dataFolder, "abilities.yml")
        if (!abilitiesFile.exists()) {
            saveResource("abilities.yml", false)
        }

        server.pluginManager.registerEvents(Thor(), this)
        logger.info("능력자 활성화")

        getCommand("능력자시작")?.setExecutor { sender, _, _, _ ->
            if (sender is Player) {
                gameManager.startGame()
                sender.sendMessage("게임이 시작되었습니다!")
            }
            true
        }
    }

    override fun onDisable() {
        logger.info("능력자 비활성화")
    }
}