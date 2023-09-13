package net.minecraft.server;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Iterator;

public class DebugReportItems implements DebugReportProvider {

    private final DebugReportGenerator b;

    public DebugReportItems(DebugReportGenerator debugreportgenerator) {
        this.b = debugreportgenerator;
    }

    public void a(HashCache hashcache) throws IOException {
        JsonObject jsonobject = new JsonObject();
        Iterator iterator = Item.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            MinecraftKey minecraftkey = (MinecraftKey) Item.REGISTRY.b(item);
            JsonObject jsonobject1 = new JsonObject();

            jsonobject1.addProperty("protocol_id", Integer.valueOf(Item.getId(item)));
            jsonobject.add(minecraftkey.toString(), jsonobject1);
        }

        java.nio.file.Path java_nio_file_path = this.b.b().resolve("reports/items.json");

        Files.createDirectories(java_nio_file_path.getParent(), new FileAttribute[0]);
        BufferedWriter bufferedwriter = Files.newBufferedWriter(java_nio_file_path, StandardCharsets.UTF_8, new OpenOption[0]);
        Throwable throwable = null;

        try {
            String s = (new GsonBuilder()).setPrettyPrinting().create().toJson(jsonobject);

            bufferedwriter.write(s);
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (bufferedwriter != null) {
                if (throwable != null) {
                    try {
                        bufferedwriter.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                } else {
                    bufferedwriter.close();
                }
            }

        }

    }

    public String a() {
        return "Item List";
    }
}
