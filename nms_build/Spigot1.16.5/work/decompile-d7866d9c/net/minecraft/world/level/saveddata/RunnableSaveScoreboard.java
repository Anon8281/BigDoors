package net.minecraft.world.level.saveddata;

public class RunnableSaveScoreboard implements Runnable {

    private final PersistentBase a;

    public RunnableSaveScoreboard(PersistentBase persistentbase) {
        this.a = persistentbase;
    }

    public void run() {
        this.a.b();
    }
}
