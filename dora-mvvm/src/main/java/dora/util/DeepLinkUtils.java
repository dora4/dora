package dora.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class DeepLinkUtils {

    public static void openDiscordUser(Context context, String userId) {
        Uri discordUri = Uri.parse("discord://users/" + userId);
        Uri webUri = Uri.parse("https://discord.com/users/" + userId);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, discordUri);
        appIntent.setPackage("com.discord");
        if (appIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(appIntent);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);
            context.startActivity(browserIntent);
        }
    }

    public static void openDiscordGroup(Context context, String inviteCode) {
        Uri discordUri = Uri.parse("discord://invite/" + inviteCode);
        Uri webUri = Uri.parse("https://discord.com/invite/" + inviteCode);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, discordUri);
        appIntent.setPackage("com.discord");
        if (appIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(appIntent);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);
            context.startActivity(browserIntent);
        }
    }
}
