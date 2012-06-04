package net.madmanmarkau.MultiHome;

import java.io.File;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MultiHome extends JavaPlugin {
	private HomeManager homes;
	private InviteManager invites;
	private WarmUpManager warmups;
	private CoolDownManager cooldowns;

	private MultiHomeCommandExecutor commandExecutor;
	private MultiHomePlayerListener playerListener = new MultiHomePlayerListener(this);
	private MultiHomeEntityListener entityListener = new MultiHomeEntityListener(this);
	
	@Override
	public void onDisable() {
		Messaging.logInfo("Version " + this.getDescription().getVersion() + " unloaded.", this);
	}

	@Override
	public void onEnable() {
		String pluginDataPath = this.getDataFolder().getAbsolutePath() + File.separator;
		
		File dataPath = new File(pluginDataPath);
		if (!dataPath.exists()) {
			dataPath.mkdirs();
		}

		this.homes = new HomeManager(new File(pluginDataPath + "homes.txt"), this);
		this.invites = new InviteManager(new File(pluginDataPath + "invites.txt"), this);
		this.warmups = new WarmUpManager(new File(pluginDataPath + "warmups.txt"), this);
		this.cooldowns = new CoolDownManager(new File(pluginDataPath + "cooldowns.txt"), this);

		setupCommands();
		
		if (!HomePermissions.initialize(this)) return;
		disableEssentials();
		Settings.initialize(this);
		Settings.loadSettings();
		MultiHomeEconManager.initialize(this);
		
		this.homes.loadHomes();
		this.invites.loadInvites();
		this.warmups.loadWarmups();
		this.cooldowns.loadCooldowns();
		
		registerEvents();
		
		Messaging.logInfo("Version " + this.getDescription().getVersion() + " loaded.", this);
	}
	
	private void disableEssentials() {
		// Disable EssentialsHome
		Plugin essentialsHome = getServer().getPluginManager().getPlugin("EssentialsHome");

		if (essentialsHome != null) {
			if (!essentialsHome.isEnabled()) {
				// Load the plugin so we can disable it. Yeah, it's weird, but hopefully works.
				getServer().getPluginManager().enablePlugin(essentialsHome);
			}
			getServer().getPluginManager().disablePlugin(essentialsHome);
		}
	}

    private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(entityListener, this);
	}
    
    private void setupCommands() {
		this.commandExecutor = new MultiHomeCommandExecutor(this);
		
    	getCommand("home").setExecutor(commandExecutor);
    	getCommand("mhome").setExecutor(commandExecutor);
    	getCommand("sethome").setExecutor(commandExecutor);
    	getCommand("msethome").setExecutor(commandExecutor);
    	getCommand("deletehome").setExecutor(commandExecutor);
    	getCommand("mdeletehome").setExecutor(commandExecutor);
    	getCommand("listhomes").setExecutor(commandExecutor);
    	getCommand("mlisthomes").setExecutor(commandExecutor);
    	getCommand("invitehome").setExecutor(commandExecutor);
    	getCommand("minvitehome").setExecutor(commandExecutor);
    	getCommand("invitehometimed").setExecutor(commandExecutor);
    	getCommand("minvitehometimed").setExecutor(commandExecutor);
    	getCommand("uninvitehome").setExecutor(commandExecutor);
    	getCommand("muninvitehome").setExecutor(commandExecutor);
    	getCommand("listinvites").setExecutor(commandExecutor);
    	getCommand("mlistinvites").setExecutor(commandExecutor);
    	getCommand("listmyinvites").setExecutor(commandExecutor);
    	getCommand("mlistmyinvites").setExecutor(commandExecutor);
    }
    
    public HomeManager getHomeManager() {
    	return homes;
    }
    
    public InviteManager getInviteManager() {
    	return invites;
    }
    
    public WarmUpManager getWarmUpManager() {
    	return warmups;
    }
    
    public CoolDownManager getCoolDownManager() {
    	return cooldowns;
    }
}
