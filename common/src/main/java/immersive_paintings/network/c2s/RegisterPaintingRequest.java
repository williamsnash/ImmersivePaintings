package immersive_paintings.network.c2s;

import immersive_paintings.Main;
import immersive_paintings.cobalt.network.Message;
import immersive_paintings.cobalt.network.NetworkHandler;
import immersive_paintings.network.s2c.PaintingListMessage;
import immersive_paintings.resources.Paintings;
import immersive_paintings.resources.ServerPaintingManager;
import immersive_paintings.util.SerializableNbt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Locale;
import java.util.Objects;

public class RegisterPaintingRequest implements Message {
    private final String name;
    private final SerializableNbt painting;

    public RegisterPaintingRequest(String name, Paintings.PaintingData painting) {
        this.name = name;
        this.painting = new SerializableNbt(painting.toFullNbt());
    }

    private String escapeString(String string) {
        return string.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_.-]", "");
    }

    @Override
    public void receive(PlayerEntity e) {
        String id = escapeString(e.getGameProfile().getName()) + "/" + escapeString(name);
        Identifier identifier = Main.locate(id);

        NbtCompound nbt = this.painting.getNbt();

        nbt.putString("author", e.getGameProfile().getName());
        nbt.putString("name", name);

        Paintings.PaintingData painting = Paintings.PaintingData.fromNbt(nbt);

        ServerPaintingManager.registerPainting(
                identifier,
                painting
        );

        //update clients
        for (ServerPlayerEntity player : Objects.requireNonNull(e.getServer()).getPlayerManager().getPlayerList()) {
            NetworkHandler.sendToPlayer(new PaintingListMessage(identifier, painting), player);
        }
    }
}
