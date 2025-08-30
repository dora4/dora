package dora.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object DeepLinkUtils {

    @JvmStatic
    fun openDiscordUser(context: Context, userId: String) {
        val discordUri = Uri.parse("discord://users/$userId")
        val webUri = Uri.parse("https://discord.com/users/$userId")
        val appIntent = Intent(Intent.ACTION_VIEW, discordUri).apply {
            `package` = "com.discord"
        }
        if (appIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(appIntent)
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(browserIntent)
        }
    }

    @JvmStatic
    fun openDiscordGroup(context: Context, inviteCode: String) {
        val discordUri = Uri.parse("discord://invite/$inviteCode")
        val webUri = Uri.parse("https://discord.com/invite/$inviteCode")

        // 尝试用 Discord App 打开
        val appIntent = Intent(Intent.ACTION_VIEW, discordUri).apply {
            `package` = "com.discord"
        }
        if (appIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(appIntent)
        } else {
            // 回退到浏览器打开
            val browserIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(browserIntent)
        }
    }
}