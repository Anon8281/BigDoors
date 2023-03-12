package nl.pim16aap2.bigDoors;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import nl.pim16aap2.bigDoors.compatibility.IPermissionsManager;
import nl.pim16aap2.bigDoors.util.DoorType;
import nl.pim16aap2.bigDoors.util.Util;
import nl.pim16aap2.jcalculator.JCalculator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public final class VaultManager implements IPermissionsManager
{
    // Try to store the price of the doors as integers, because that's faster than
    // evaluating the formula.
    Integer doorPrice, drawbridgePrice, portcullisPrice, slidingDoorPrice;
    private final static double EPSILON = 0.00001;

    private final BigDoors plugin;
    private final HashMap<Long, Double> menu;
    private final boolean vaultEnabled;

    private Economy economy = null;
    private Permission perms = null;

    public VaultManager(final BigDoors plugin)
    {
        this.plugin = plugin;
        menu = new HashMap<>();
        init();
        vaultEnabled = setupEconomy() && setupPermissions();
    }

    public void refundDoor(Door door)
    {
        if (!vaultEnabled || !plugin.getConfigLoader().refundOnDelete())
            return;
        final double price = getPrice(door.getType(), door.getBlockCount());
        economy.depositPlayer(Bukkit.getOfflinePlayer(door.getPrimeOwner()), door.getWorld().getName(), price);
    }

    public boolean buyDoor(final Player player, final DoorType type, final int blockCount)
    {
        if (!vaultEnabled)
            return true;
        double price = getPrice(type, blockCount);
        if (price < EPSILON)
            return true;

        if (withdrawPlayer(player, player.getWorld().getName(), price))
        {
            if (price > 0)
                Util.messagePlayer(player, plugin.getMessages().getString("CREATOR.GENERAL.MoneyWithdrawn") + " " + price);
            return true;
        }

        Util.messagePlayer(player, plugin.getMessages().getString("CREATOR.GENERAL.InsufficientFunds") + " " + price);
        return false;
    }

    public void init()
    {
        menu.clear();
        try
        {
            doorPrice = Integer.parseInt(plugin.getConfigLoader().doorPrice());
        }
        catch(Exception e)
        {
            doorPrice = null;
        }

        try
        {
            drawbridgePrice = Integer.parseInt(plugin.getConfigLoader().drawbridgePrice());
        }
        catch(Exception e)
        {
            drawbridgePrice = null;
        }

        try
        {
            portcullisPrice = Integer.parseInt(plugin.getConfigLoader().portcullisPrice());
        }
        catch(Exception e)
        {
            portcullisPrice = null;
        }

        try
        {
            slidingDoorPrice = Integer.parseInt(plugin.getConfigLoader().slidingDoorPrice());
        }
        catch(Exception e)
        {
            slidingDoorPrice = null;
        }
    }

    public boolean hasPermission(Player player, String permission)
    {
        return vaultEnabled && perms.playerHas(player.getWorld().getName(), player, permission);
    }

    public CompletableFuture<Boolean> hasPermission(OfflinePlayer player, String permission, String worldName)
    {
        if (!vaultEnabled)
            return CompletableFuture.completedFuture(false);
        return CompletableFuture
            .supplyAsync(() -> perms.playerHas(worldName, player, permission))
            .exceptionally(throwable -> Util.exceptionally(throwable, false));
    }

    private double evaluateFormula(String formula, int blockCount)
    {
        try
        {
            return JCalculator.getResult(formula, new String[]{"blockCount"}, new double[]{blockCount});
        }
        catch (Exception e)
        {
            plugin.getMyLogger().logMessageToConsole("Failed to determine door creation price! Please contact pim16aap2! Include this: \"" + formula + "\"");
            e.printStackTrace();
            return 0;
        }
    }

    public double getPrice(final DoorType type, final int blockCount)
    {
        if (!vaultEnabled)
            return 0;

        // Try cache first
        long priceID = blockCount * 100L + DoorType.getValue(type);
        if (menu.containsKey(priceID))
            return menu.get(priceID);

        double price = 0;

        switch(type)
        {
        case DOOR:
            if (doorPrice != null)
                price = doorPrice;
            else
                price = evaluateFormula(plugin.getConfigLoader().doorPrice(), blockCount);
            break;

        case DRAWBRIDGE:
            if (drawbridgePrice != null)
                price = drawbridgePrice;
            else
                price = evaluateFormula(plugin.getConfigLoader().drawbridgePrice(), blockCount);
            break;

        case PORTCULLIS:
            if (portcullisPrice != null)
                price = portcullisPrice;
            else
                price = evaluateFormula(plugin.getConfigLoader().portcullisPrice(), blockCount);
            break;

        case SLIDINGDOOR:
            if (slidingDoorPrice != null)
                price = slidingDoorPrice;
            else
                price = evaluateFormula(plugin.getConfigLoader().slidingDoorPrice(), blockCount);
            break;
        }

        // Negative values aren't allowed.
        price = Math.max(0, price);
        menu.put(priceID, price);
        return price;
    }

    private boolean has(final OfflinePlayer player, final double amount)
    {
        if (amount < EPSILON)
            return true;
        try
        {
            return economy.has(player, amount);
        }
        catch(Exception e)
        {
            plugin.getMyLogger().warn("Failed to check balance of player \"" + player.getName() + "\" (" + player.getUniqueId() + ")! Please contact pim16aap2!");
            plugin.getMyLogger().logMessageToLogFile(Util.throwableToString(e));
        }
        return true;
    }

    private boolean withdrawPlayer(final OfflinePlayer player, final String worldName, final double amount)
    {
        if (amount < EPSILON)
            return true;
        try
        {
            if (has(player, amount))
                return economy.withdrawPlayer(player, worldName, amount).type.equals(EconomyResponse.ResponseType.SUCCESS);
            return false;
        }
        catch(Exception e)
        {
            plugin.getMyLogger().warn("Failed to subtract money from player \"" + player.getName() + "\" (" + player.getUniqueId() + ")! Please contact pim16aap2!");
            plugin.getMyLogger().logMessageToLogFile(Util.throwableToString(e));
        }
        return true;
    }

    private boolean setupEconomy()
    {
        try
        {
            if (plugin.getServer().getPluginManager().getPlugin("Vault") == null)
                return false;
            RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null)
                economy = economyProvider.getProvider();

            return (economy != null);
        }
        catch (Exception e)
        {
            plugin.getMyLogger().logMessage("Exception encountered while initiating Vault dependency! It will be disabled! Please contact pim16aap2!", true, false);
            plugin.getMyLogger().logMessageToLogFile(Util.throwableToString(e));
            return false;
        }
    }

    private boolean setupPermissions()
    {
        try
        {
            RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
            perms = rsp.getProvider();
            return perms != null;
        }
        catch (Exception e)
        {
            plugin.getMyLogger().logMessage("Exception encountered while initiating Vault dependency! It will be disabled! Please contact pim16aap2!", true, false);
            plugin.getMyLogger().logMessageToLogFile(Util.throwableToString(e));
            return false;
        }
    }
}
