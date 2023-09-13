package net.minecraft.server;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ParticleParamItem implements ParticleParam {

    public static final ParticleParam.a<ParticleParamItem> a = new ParticleParam.a() {
        public ParticleParamItem a(Particle<ParticleParamItem> particle, StringReader stringreader) throws CommandSyntaxException {
            stringreader.expect(' ');
            ArgumentParserItemStack argumentparseritemstack = (new ArgumentParserItemStack(stringreader, false)).h();
            ItemStack itemstack = (new ArgumentPredicateItemStack(argumentparseritemstack.b(), argumentparseritemstack.c())).a(1, false);

            return new ParticleParamItem(particle, itemstack);
        }

        public ParticleParamItem a(Particle<ParticleParamItem> particle, PacketDataSerializer packetdataserializer) {
            return new ParticleParamItem(particle, packetdataserializer.k());
        }

        public ParticleParam b(Particle particle, PacketDataSerializer packetdataserializer) {
            return this.a(particle, packetdataserializer);
        }

        public ParticleParam b(Particle particle, StringReader stringreader) throws CommandSyntaxException {
            return this.a(particle, stringreader);
        }
    };
    private final Particle<ParticleParamItem> b;
    private final ItemStack c;

    public ParticleParamItem(Particle<ParticleParamItem> particle, ItemStack itemstack) {
        this.b = particle;
        this.c = itemstack;
    }

    public void a(PacketDataSerializer packetdataserializer) {
        packetdataserializer.a(this.c);
    }

    public String a() {
        return this.b().d() + " " + (new ArgumentPredicateItemStack(this.c.getItem(), this.c.getTag())).c();
    }

    public Particle<ParticleParamItem> b() {
        return this.b;
    }
}
