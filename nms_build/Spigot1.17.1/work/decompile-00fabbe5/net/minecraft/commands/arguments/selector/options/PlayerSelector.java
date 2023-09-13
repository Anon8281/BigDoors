package net.minecraft.commands.arguments.selector.options;

import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.critereon.CriterionConditionRange;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.commands.ICompletionProvider;
import net.minecraft.commands.arguments.selector.ArgumentParserSelector;
import net.minecraft.core.IRegistry;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.AdvancementDataPlayer;
import net.minecraft.server.AdvancementDataWorld;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.tags.TagsEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameterSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameters;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardScore;
import net.minecraft.world.scores.ScoreboardTeamBase;

public class PlayerSelector {

    private static final Map<String, PlayerSelector.b> OPTIONS = Maps.newHashMap();
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_OPTION = new DynamicCommandExceptionType((object) -> {
        return new ChatMessage("argument.entity.options.unknown", new Object[]{object});
    });
    public static final DynamicCommandExceptionType ERROR_INAPPLICABLE_OPTION = new DynamicCommandExceptionType((object) -> {
        return new ChatMessage("argument.entity.options.inapplicable", new Object[]{object});
    });
    public static final SimpleCommandExceptionType ERROR_RANGE_NEGATIVE = new SimpleCommandExceptionType(new ChatMessage("argument.entity.options.distance.negative"));
    public static final SimpleCommandExceptionType ERROR_LEVEL_NEGATIVE = new SimpleCommandExceptionType(new ChatMessage("argument.entity.options.level.negative"));
    public static final SimpleCommandExceptionType ERROR_LIMIT_TOO_SMALL = new SimpleCommandExceptionType(new ChatMessage("argument.entity.options.limit.toosmall"));
    public static final DynamicCommandExceptionType ERROR_SORT_UNKNOWN = new DynamicCommandExceptionType((object) -> {
        return new ChatMessage("argument.entity.options.sort.irreversible", new Object[]{object});
    });
    public static final DynamicCommandExceptionType ERROR_GAME_MODE_INVALID = new DynamicCommandExceptionType((object) -> {
        return new ChatMessage("argument.entity.options.mode.invalid", new Object[]{object});
    });
    public static final DynamicCommandExceptionType ERROR_ENTITY_TYPE_INVALID = new DynamicCommandExceptionType((object) -> {
        return new ChatMessage("argument.entity.options.type.invalid", new Object[]{object});
    });

    public PlayerSelector() {}

    private static void a(String s, PlayerSelector.a playerselector_a, Predicate<ArgumentParserSelector> predicate, IChatBaseComponent ichatbasecomponent) {
        PlayerSelector.OPTIONS.put(s, new PlayerSelector.b(playerselector_a, predicate, ichatbasecomponent));
    }

    public static void a() {
        if (PlayerSelector.OPTIONS.isEmpty()) {
            a("name", (argumentparserselector) -> {
                int i = argumentparserselector.g().getCursor();
                boolean flag = argumentparserselector.e();
                String s = argumentparserselector.g().readString();

                if (argumentparserselector.w() && !flag) {
                    argumentparserselector.g().setCursor(i);
                    throw PlayerSelector.ERROR_INAPPLICABLE_OPTION.createWithContext(argumentparserselector.g(), "name");
                } else {
                    if (flag) {
                        argumentparserselector.c(true);
                    } else {
                        argumentparserselector.b(true);
                    }

                    argumentparserselector.a((entity) -> {
                        return entity.getDisplayName().getString().equals(s) != flag;
                    });
                }
            }, (argumentparserselector) -> {
                return !argumentparserselector.v();
            }, new ChatMessage("argument.entity.options.name.description"));
            a("distance", (argumentparserselector) -> {
                int i = argumentparserselector.g().getCursor();
                CriterionConditionValue.DoubleRange criterionconditionvalue_doublerange = CriterionConditionValue.DoubleRange.a(argumentparserselector.g());

                if ((criterionconditionvalue_doublerange.a() == null || (Double) criterionconditionvalue_doublerange.a() >= 0.0D) && (criterionconditionvalue_doublerange.b() == null || (Double) criterionconditionvalue_doublerange.b() >= 0.0D)) {
                    argumentparserselector.a(criterionconditionvalue_doublerange);
                    argumentparserselector.h();
                } else {
                    argumentparserselector.g().setCursor(i);
                    throw PlayerSelector.ERROR_RANGE_NEGATIVE.createWithContext(argumentparserselector.g());
                }
            }, (argumentparserselector) -> {
                return argumentparserselector.i().c();
            }, new ChatMessage("argument.entity.options.distance.description"));
            a("level", (argumentparserselector) -> {
                int i = argumentparserselector.g().getCursor();
                CriterionConditionValue.IntegerRange criterionconditionvalue_integerrange = CriterionConditionValue.IntegerRange.a(argumentparserselector.g());

                if ((criterionconditionvalue_integerrange.a() == null || (Integer) criterionconditionvalue_integerrange.a() >= 0) && (criterionconditionvalue_integerrange.b() == null || (Integer) criterionconditionvalue_integerrange.b() >= 0)) {
                    argumentparserselector.a(criterionconditionvalue_integerrange);
                    argumentparserselector.a(false);
                } else {
                    argumentparserselector.g().setCursor(i);
                    throw PlayerSelector.ERROR_LEVEL_NEGATIVE.createWithContext(argumentparserselector.g());
                }
            }, (argumentparserselector) -> {
                return argumentparserselector.j().c();
            }, new ChatMessage("argument.entity.options.level.description"));
            a("x", (argumentparserselector) -> {
                argumentparserselector.h();
                argumentparserselector.a(argumentparserselector.g().readDouble());
            }, (argumentparserselector) -> {
                return argumentparserselector.m() == null;
            }, new ChatMessage("argument.entity.options.x.description"));
            a("y", (argumentparserselector) -> {
                argumentparserselector.h();
                argumentparserselector.b(argumentparserselector.g().readDouble());
            }, (argumentparserselector) -> {
                return argumentparserselector.n() == null;
            }, new ChatMessage("argument.entity.options.y.description"));
            a("z", (argumentparserselector) -> {
                argumentparserselector.h();
                argumentparserselector.c(argumentparserselector.g().readDouble());
            }, (argumentparserselector) -> {
                return argumentparserselector.o() == null;
            }, new ChatMessage("argument.entity.options.z.description"));
            a("dx", (argumentparserselector) -> {
                argumentparserselector.h();
                argumentparserselector.d(argumentparserselector.g().readDouble());
            }, (argumentparserselector) -> {
                return argumentparserselector.p() == null;
            }, new ChatMessage("argument.entity.options.dx.description"));
            a("dy", (argumentparserselector) -> {
                argumentparserselector.h();
                argumentparserselector.e(argumentparserselector.g().readDouble());
            }, (argumentparserselector) -> {
                return argumentparserselector.q() == null;
            }, new ChatMessage("argument.entity.options.dy.description"));
            a("dz", (argumentparserselector) -> {
                argumentparserselector.h();
                argumentparserselector.f(argumentparserselector.g().readDouble());
            }, (argumentparserselector) -> {
                return argumentparserselector.r() == null;
            }, new ChatMessage("argument.entity.options.dz.description"));
            a("x_rotation", (argumentparserselector) -> {
                argumentparserselector.a(CriterionConditionRange.a(argumentparserselector.g(), true, MathHelper::g));
            }, (argumentparserselector) -> {
                return argumentparserselector.k() == CriterionConditionRange.ANY;
            }, new ChatMessage("argument.entity.options.x_rotation.description"));
            a("y_rotation", (argumentparserselector) -> {
                argumentparserselector.b(CriterionConditionRange.a(argumentparserselector.g(), true, MathHelper::g));
            }, (argumentparserselector) -> {
                return argumentparserselector.l() == CriterionConditionRange.ANY;
            }, new ChatMessage("argument.entity.options.y_rotation.description"));
            a("limit", (argumentparserselector) -> {
                int i = argumentparserselector.g().getCursor();
                int j = argumentparserselector.g().readInt();

                if (j < 1) {
                    argumentparserselector.g().setCursor(i);
                    throw PlayerSelector.ERROR_LIMIT_TOO_SMALL.createWithContext(argumentparserselector.g());
                } else {
                    argumentparserselector.a(j);
                    argumentparserselector.d(true);
                }
            }, (argumentparserselector) -> {
                return !argumentparserselector.u() && !argumentparserselector.x();
            }, new ChatMessage("argument.entity.options.limit.description"));
            a("sort", (argumentparserselector) -> {
                int i = argumentparserselector.g().getCursor();
                String s = argumentparserselector.g().readUnquotedString();

                argumentparserselector.a((suggestionsbuilder, consumer) -> {
                    return ICompletionProvider.b((Iterable) Arrays.asList("nearest", "furthest", "random", "arbitrary"), suggestionsbuilder);
                });
                byte b0 = -1;

                switch (s.hashCode()) {
                    case -938285885:
                        if (s.equals("random")) {
                            b0 = 2;
                        }
                        break;
                    case 1510793967:
                        if (s.equals("furthest")) {
                            b0 = 1;
                        }
                        break;
                    case 1780188658:
                        if (s.equals("arbitrary")) {
                            b0 = 3;
                        }
                        break;
                    case 1825779806:
                        if (s.equals("nearest")) {
                            b0 = 0;
                        }
                }

                BiConsumer biconsumer;

                switch (b0) {
                    case 0:
                        biconsumer = ArgumentParserSelector.ORDER_NEAREST;
                        break;
                    case 1:
                        biconsumer = ArgumentParserSelector.ORDER_FURTHEST;
                        break;
                    case 2:
                        biconsumer = ArgumentParserSelector.ORDER_RANDOM;
                        break;
                    case 3:
                        biconsumer = ArgumentParserSelector.ORDER_ARBITRARY;
                        break;
                    default:
                        argumentparserselector.g().setCursor(i);
                        throw PlayerSelector.ERROR_SORT_UNKNOWN.createWithContext(argumentparserselector.g(), s);
                }

                argumentparserselector.a(biconsumer);
                argumentparserselector.e(true);
            }, (argumentparserselector) -> {
                return !argumentparserselector.u() && !argumentparserselector.y();
            }, new ChatMessage("argument.entity.options.sort.description"));
            a("gamemode", (argumentparserselector) -> {
                argumentparserselector.a((suggestionsbuilder, consumer) -> {
                    String s = suggestionsbuilder.getRemaining().toLowerCase(Locale.ROOT);
                    boolean flag = !argumentparserselector.A();
                    boolean flag1 = true;

                    if (!s.isEmpty()) {
                        if (s.charAt(0) == '!') {
                            flag = false;
                            s = s.substring(1);
                        } else {
                            flag1 = false;
                        }
                    }

                    EnumGamemode[] aenumgamemode = EnumGamemode.values();
                    int i = aenumgamemode.length;

                    for (int j = 0; j < i; ++j) {
                        EnumGamemode enumgamemode = aenumgamemode[j];

                        if (enumgamemode.b().toLowerCase(Locale.ROOT).startsWith(s)) {
                            if (flag1) {
                                suggestionsbuilder.suggest("!" + enumgamemode.b());
                            }

                            if (flag) {
                                suggestionsbuilder.suggest(enumgamemode.b());
                            }
                        }
                    }

                    return suggestionsbuilder.buildFuture();
                });
                int i = argumentparserselector.g().getCursor();
                boolean flag = argumentparserselector.e();

                if (argumentparserselector.A() && !flag) {
                    argumentparserselector.g().setCursor(i);
                    throw PlayerSelector.ERROR_INAPPLICABLE_OPTION.createWithContext(argumentparserselector.g(), "gamemode");
                } else {
                    String s = argumentparserselector.g().readUnquotedString();
                    EnumGamemode enumgamemode = EnumGamemode.a(s, (EnumGamemode) null);

                    if (enumgamemode == null) {
                        argumentparserselector.g().setCursor(i);
                        throw PlayerSelector.ERROR_GAME_MODE_INVALID.createWithContext(argumentparserselector.g(), s);
                    } else {
                        argumentparserselector.a(false);
                        argumentparserselector.a((entity) -> {
                            if (!(entity instanceof EntityPlayer)) {
                                return false;
                            } else {
                                EnumGamemode enumgamemode1 = ((EntityPlayer) entity).gameMode.getGameMode();

                                return flag ? enumgamemode1 != enumgamemode : enumgamemode1 == enumgamemode;
                            }
                        });
                        if (flag) {
                            argumentparserselector.g(true);
                        } else {
                            argumentparserselector.f(true);
                        }

                    }
                }
            }, (argumentparserselector) -> {
                return !argumentparserselector.z();
            }, new ChatMessage("argument.entity.options.gamemode.description"));
            a("team", (argumentparserselector) -> {
                boolean flag = argumentparserselector.e();
                String s = argumentparserselector.g().readUnquotedString();

                argumentparserselector.a((entity) -> {
                    if (!(entity instanceof EntityLiving)) {
                        return false;
                    } else {
                        ScoreboardTeamBase scoreboardteambase = entity.getScoreboardTeam();
                        String s1 = scoreboardteambase == null ? "" : scoreboardteambase.getName();

                        return s1.equals(s) != flag;
                    }
                });
                if (flag) {
                    argumentparserselector.i(true);
                } else {
                    argumentparserselector.h(true);
                }

            }, (argumentparserselector) -> {
                return !argumentparserselector.B();
            }, new ChatMessage("argument.entity.options.team.description"));
            a("type", (argumentparserselector) -> {
                argumentparserselector.a((suggestionsbuilder, consumer) -> {
                    ICompletionProvider.a((Iterable) IRegistry.ENTITY_TYPE.keySet(), suggestionsbuilder, String.valueOf('!'));
                    ICompletionProvider.a((Iterable) TagsEntity.a().b(), suggestionsbuilder, "!#");
                    if (!argumentparserselector.F()) {
                        ICompletionProvider.a((Iterable) IRegistry.ENTITY_TYPE.keySet(), suggestionsbuilder);
                        ICompletionProvider.a((Iterable) TagsEntity.a().b(), suggestionsbuilder, String.valueOf('#'));
                    }

                    return suggestionsbuilder.buildFuture();
                });
                int i = argumentparserselector.g().getCursor();
                boolean flag = argumentparserselector.e();

                if (argumentparserselector.F() && !flag) {
                    argumentparserselector.g().setCursor(i);
                    throw PlayerSelector.ERROR_INAPPLICABLE_OPTION.createWithContext(argumentparserselector.g(), "type");
                } else {
                    if (flag) {
                        argumentparserselector.D();
                    }

                    MinecraftKey minecraftkey;

                    if (argumentparserselector.f()) {
                        minecraftkey = MinecraftKey.a(argumentparserselector.g());
                        argumentparserselector.a((entity) -> {
                            return entity.getEntityType().a(entity.getMinecraftServer().getTagRegistry().a(IRegistry.ENTITY_TYPE_REGISTRY).b(minecraftkey)) != flag;
                        });
                    } else {
                        minecraftkey = MinecraftKey.a(argumentparserselector.g());
                        EntityTypes<?> entitytypes = (EntityTypes) IRegistry.ENTITY_TYPE.getOptional(minecraftkey).orElseThrow(() -> {
                            argumentparserselector.g().setCursor(i);
                            return PlayerSelector.ERROR_ENTITY_TYPE_INVALID.createWithContext(argumentparserselector.g(), minecraftkey.toString());
                        });

                        if (Objects.equals(EntityTypes.PLAYER, entitytypes) && !flag) {
                            argumentparserselector.a(false);
                        }

                        argumentparserselector.a((entity) -> {
                            return Objects.equals(entitytypes, entity.getEntityType()) != flag;
                        });
                        if (!flag) {
                            argumentparserselector.a(entitytypes);
                        }
                    }

                }
            }, (argumentparserselector) -> {
                return !argumentparserselector.E();
            }, new ChatMessage("argument.entity.options.type.description"));
            a("tag", (argumentparserselector) -> {
                boolean flag = argumentparserselector.e();
                String s = argumentparserselector.g().readUnquotedString();

                argumentparserselector.a((entity) -> {
                    return "".equals(s) ? entity.getScoreboardTags().isEmpty() != flag : entity.getScoreboardTags().contains(s) != flag;
                });
            }, (argumentparserselector) -> {
                return true;
            }, new ChatMessage("argument.entity.options.tag.description"));
            a("nbt", (argumentparserselector) -> {
                boolean flag = argumentparserselector.e();
                NBTTagCompound nbttagcompound = (new MojangsonParser(argumentparserselector.g())).f();

                argumentparserselector.a((entity) -> {
                    NBTTagCompound nbttagcompound1 = entity.save(new NBTTagCompound());

                    if (entity instanceof EntityPlayer) {
                        ItemStack itemstack = ((EntityPlayer) entity).getInventory().getItemInHand();

                        if (!itemstack.isEmpty()) {
                            nbttagcompound1.set("SelectedItem", itemstack.save(new NBTTagCompound()));
                        }
                    }

                    return GameProfileSerializer.a(nbttagcompound, nbttagcompound1, true) != flag;
                });
            }, (argumentparserselector) -> {
                return true;
            }, new ChatMessage("argument.entity.options.nbt.description"));
            a("scores", (argumentparserselector) -> {
                StringReader stringreader = argumentparserselector.g();
                Map<String, CriterionConditionValue.IntegerRange> map = Maps.newHashMap();

                stringreader.expect('{');
                stringreader.skipWhitespace();

                while (stringreader.canRead() && stringreader.peek() != '}') {
                    stringreader.skipWhitespace();
                    String s = stringreader.readUnquotedString();

                    stringreader.skipWhitespace();
                    stringreader.expect('=');
                    stringreader.skipWhitespace();
                    CriterionConditionValue.IntegerRange criterionconditionvalue_integerrange = CriterionConditionValue.IntegerRange.a(stringreader);

                    map.put(s, criterionconditionvalue_integerrange);
                    stringreader.skipWhitespace();
                    if (stringreader.canRead() && stringreader.peek() == ',') {
                        stringreader.skip();
                    }
                }

                stringreader.expect('}');
                if (!map.isEmpty()) {
                    argumentparserselector.a((entity) -> {
                        ScoreboardServer scoreboardserver = entity.getMinecraftServer().getScoreboard();
                        String s1 = entity.getName();
                        Iterator iterator = map.entrySet().iterator();

                        Entry entry;
                        int i;

                        do {
                            if (!iterator.hasNext()) {
                                return true;
                            }

                            entry = (Entry) iterator.next();
                            ScoreboardObjective scoreboardobjective = scoreboardserver.getObjective((String) entry.getKey());

                            if (scoreboardobjective == null) {
                                return false;
                            }

                            if (!scoreboardserver.b(s1, scoreboardobjective)) {
                                return false;
                            }

                            ScoreboardScore scoreboardscore = scoreboardserver.getPlayerScoreForObjective(s1, scoreboardobjective);

                            i = scoreboardscore.getScore();
                        } while (((CriterionConditionValue.IntegerRange) entry.getValue()).d(i));

                        return false;
                    });
                }

                argumentparserselector.j(true);
            }, (argumentparserselector) -> {
                return !argumentparserselector.G();
            }, new ChatMessage("argument.entity.options.scores.description"));
            a("advancements", (argumentparserselector) -> {
                StringReader stringreader = argumentparserselector.g();
                Map<MinecraftKey, Predicate<AdvancementProgress>> map = Maps.newHashMap();

                stringreader.expect('{');
                stringreader.skipWhitespace();

                while (stringreader.canRead() && stringreader.peek() != '}') {
                    stringreader.skipWhitespace();
                    MinecraftKey minecraftkey = MinecraftKey.a(stringreader);

                    stringreader.skipWhitespace();
                    stringreader.expect('=');
                    stringreader.skipWhitespace();
                    if (stringreader.canRead() && stringreader.peek() == '{') {
                        Map<String, Predicate<CriterionProgress>> map1 = Maps.newHashMap();

                        stringreader.skipWhitespace();
                        stringreader.expect('{');
                        stringreader.skipWhitespace();

                        while (stringreader.canRead() && stringreader.peek() != '}') {
                            stringreader.skipWhitespace();
                            String s = stringreader.readUnquotedString();

                            stringreader.skipWhitespace();
                            stringreader.expect('=');
                            stringreader.skipWhitespace();
                            boolean flag = stringreader.readBoolean();

                            map1.put(s, (criterionprogress) -> {
                                return criterionprogress.a() == flag;
                            });
                            stringreader.skipWhitespace();
                            if (stringreader.canRead() && stringreader.peek() == ',') {
                                stringreader.skip();
                            }
                        }

                        stringreader.skipWhitespace();
                        stringreader.expect('}');
                        stringreader.skipWhitespace();
                        map.put(minecraftkey, (advancementprogress) -> {
                            Iterator iterator = map1.entrySet().iterator();

                            Entry entry;
                            CriterionProgress criterionprogress;

                            do {
                                if (!iterator.hasNext()) {
                                    return true;
                                }

                                entry = (Entry) iterator.next();
                                criterionprogress = advancementprogress.getCriterionProgress((String) entry.getKey());
                            } while (criterionprogress != null && ((Predicate) entry.getValue()).test(criterionprogress));

                            return false;
                        });
                    } else {
                        boolean flag1 = stringreader.readBoolean();

                        map.put(minecraftkey, (advancementprogress) -> {
                            return advancementprogress.isDone() == flag1;
                        });
                    }

                    stringreader.skipWhitespace();
                    if (stringreader.canRead() && stringreader.peek() == ',') {
                        stringreader.skip();
                    }
                }

                stringreader.expect('}');
                if (!map.isEmpty()) {
                    argumentparserselector.a((entity) -> {
                        if (!(entity instanceof EntityPlayer)) {
                            return false;
                        } else {
                            EntityPlayer entityplayer = (EntityPlayer) entity;
                            AdvancementDataPlayer advancementdataplayer = entityplayer.getAdvancementData();
                            AdvancementDataWorld advancementdataworld = entityplayer.getMinecraftServer().getAdvancementData();
                            Iterator iterator = map.entrySet().iterator();

                            Entry entry;
                            Advancement advancement;

                            do {
                                if (!iterator.hasNext()) {
                                    return true;
                                }

                                entry = (Entry) iterator.next();
                                advancement = advancementdataworld.a((MinecraftKey) entry.getKey());
                            } while (advancement != null && ((Predicate) entry.getValue()).test(advancementdataplayer.getProgress(advancement)));

                            return false;
                        }
                    });
                    argumentparserselector.a(false);
                }

                argumentparserselector.k(true);
            }, (argumentparserselector) -> {
                return !argumentparserselector.H();
            }, new ChatMessage("argument.entity.options.advancements.description"));
            a("predicate", (argumentparserselector) -> {
                boolean flag = argumentparserselector.e();
                MinecraftKey minecraftkey = MinecraftKey.a(argumentparserselector.g());

                argumentparserselector.a((entity) -> {
                    if (!(entity.level instanceof WorldServer)) {
                        return false;
                    } else {
                        WorldServer worldserver = (WorldServer) entity.level;
                        LootItemCondition lootitemcondition = worldserver.getMinecraftServer().getLootPredicateManager().a(minecraftkey);

                        if (lootitemcondition == null) {
                            return false;
                        } else {
                            LootTableInfo loottableinfo = (new LootTableInfo.Builder(worldserver)).set(LootContextParameters.THIS_ENTITY, entity).set(LootContextParameters.ORIGIN, entity.getPositionVector()).build(LootContextParameterSets.SELECTOR);

                            return flag ^ lootitemcondition.test(loottableinfo);
                        }
                    }
                });
            }, (argumentparserselector) -> {
                return true;
            }, new ChatMessage("argument.entity.options.predicate.description"));
        }
    }

    public static PlayerSelector.a a(ArgumentParserSelector argumentparserselector, String s, int i) throws CommandSyntaxException {
        PlayerSelector.b playerselector_b = (PlayerSelector.b) PlayerSelector.OPTIONS.get(s);

        if (playerselector_b != null) {
            if (playerselector_b.predicate.test(argumentparserselector)) {
                return playerselector_b.modifier;
            } else {
                throw PlayerSelector.ERROR_INAPPLICABLE_OPTION.createWithContext(argumentparserselector.g(), s);
            }
        } else {
            argumentparserselector.g().setCursor(i);
            throw PlayerSelector.ERROR_UNKNOWN_OPTION.createWithContext(argumentparserselector.g(), s);
        }
    }

    public static void a(ArgumentParserSelector argumentparserselector, SuggestionsBuilder suggestionsbuilder) {
        String s = suggestionsbuilder.getRemaining().toLowerCase(Locale.ROOT);
        Iterator iterator = PlayerSelector.OPTIONS.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, PlayerSelector.b> entry = (Entry) iterator.next();

            if (((PlayerSelector.b) entry.getValue()).predicate.test(argumentparserselector) && ((String) entry.getKey()).toLowerCase(Locale.ROOT).startsWith(s)) {
                suggestionsbuilder.suggest((String) entry.getKey() + "=", ((PlayerSelector.b) entry.getValue()).description);
            }
        }

    }

    private static class b {

        public final PlayerSelector.a modifier;
        public final Predicate<ArgumentParserSelector> predicate;
        public final IChatBaseComponent description;

        b(PlayerSelector.a playerselector_a, Predicate<ArgumentParserSelector> predicate, IChatBaseComponent ichatbasecomponent) {
            this.modifier = playerselector_a;
            this.predicate = predicate;
            this.description = ichatbasecomponent;
        }
    }

    public interface a {

        void handle(ArgumentParserSelector argumentparserselector) throws CommandSyntaxException;
    }
}
