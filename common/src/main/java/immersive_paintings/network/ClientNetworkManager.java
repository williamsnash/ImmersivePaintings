package immersive_paintings.network;

import immersive_paintings.entity.ImmersivePaintingEntity;
import immersive_paintings.network.s2c.ImmersivePaintingSpawnMessage;
import immersive_paintings.network.s2c.OpenGuiRequest;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public class ClientNetworkManager implements NetworkManager {
    @Override
    public void handleImmersivePaintingSpawnMessage(ImmersivePaintingSpawnMessage message) {
        ClientWorld world = MinecraftClient.getInstance().world;
        assert world != null;

        ImmersivePaintingEntity painting = new ImmersivePaintingEntity(world, message.pos(), message.facing());
        painting.setId(message.id());
        painting.setUuid(message.uuid());

        world.addEntity(message.id(), painting);
    }

    @Override
    public void handleOpenGuiRequest(OpenGuiRequest request) {

    }
}
